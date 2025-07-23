import React, {useEffect, useState} from "react";
import {Button, Checkbox, Form, message, Popconfirm, Select, Space, Table} from "antd";
import {
    createTransportation,
    deleteTransportation,
    getTransportations,
    type Transportation,
    updateTransportation,
} from "../../services/transportation-service";
import {getLocations, type Location} from "../../services/location-service";
import {useAuth} from "../../context/auth-context.tsx";

const {Option} = Select;

const daysOfWeek = [
    {label: "Mon", value: 1},
    {label: "Tue", value: 2},
    {label: "Wed", value: 3},
    {label: "Thu", value: 4},
    {label: "Fri", value: 5},
    {label: "Sat", value: 6},
    {label: "Sun", value: 7},
];
const TransportationPage: React.FC = () => {
    const [transportations, setTransportations] = useState<Transportation[]>([]);
    const [locations, setLocations] = useState<Location[]>([]);
    const [form] = Form.useForm();
    const [editingId, setEditingId] = useState<string | null>(null);
    const {token} = useAuth();

    useEffect(() => {
        const fetchLocations = async () => {
            try {
                const data = await getLocations(token);
                setLocations(data.content);
            } catch {
                message.error("Failed to load locations.");
            }
        };

        const fetchTransportations = async () => {
            try {
                const data = await getTransportations(token);
                setTransportations(data.content);
            } catch {
                message.error("Failed to fetch transportation data.");
            }
        };

        fetchLocations();
        fetchTransportations();
    }, []);

    const handleAdd = async (values: any) => {
        try {
            const newItem = await createTransportation(values, token);
            setTransportations([...transportations, newItem]);
            message.success("Added successfully.");
            form.resetFields();
        } catch {
            message.error("Failed to add.");
        }
    };

    const handleUpdate = async (values: any) => {
        try {
            if (!editingId) return;
            await updateTransportation(editingId, values, token);
            setTransportations((prev) =>
                prev.map((t) => (t.id === editingId ? {...t, ...values} : t))
            );
            message.success("Updated successfully.");
            setEditingId(null);
            form.resetFields();
        } catch {
            message.error("Failed to update.");
        }
    };

    const handleDelete = async (id: string) => {
        try {
            await deleteTransportation(id, token);
            setTransportations(transportations.filter((t) => t.id !== id));
            message.success("Deleted successfully.");
        } catch {
            message.error("Failed to delete.");
        }
    };

    const handleEdit = (record: any) => {
        form.setFieldsValue(record);
        setEditingId(record.id);
    };

    const handleCancelEdit = () => {
        setEditingId(null);
        form.resetFields();
    };

    const columns = [
        {
            title: "Origin",
            dataIndex: "originLocationCode",
            key: "originLocationCode",
        },
        {
            title: "Destination",
            dataIndex: "destinationLocationCode",
            key: "destinationLocationCode",
        },
        {
            title: "Type",
            dataIndex: "type",
            key: "type",
        },
        {
            title: "Operating Days",
            dataIndex: "operatingDays",
            key: "operatingDays",
            render: (days: number[]) =>
                days
                    .map((day) => daysOfWeek.find((d) => d.value === day)?.label)
                    .join(", "),
        },
        {
            title: "Actions",
            key: "actions",
            render: (_: any, record: any) => (
                <Space>
                    <Button type="link" onClick={() => handleEdit(record)}>
                        Edit
                    </Button>
                    <Popconfirm
                        title="Are you sure to delete?"
                        onConfirm={() => handleDelete(record.id)}
                    >
                        <Button danger type="link">
                            Delete
                        </Button>
                    </Popconfirm>
                </Space>
            ),
        },
    ];

    return (
        <div>
            <h2>Transportation</h2>

            <Form
                form={form}
                layout="inline"
                onFinish={editingId ? handleUpdate : handleAdd}
                style={{marginBottom: 16}}
            >
                <Form.Item name="originLocationCode" rules={[{required: true}]}>
                    <Select placeholder="Select Origin" style={{width: 180}}>
                        {locations.map((loc) => (
                            <Option key={loc.id} value={loc.code}>
                                {loc.name} ({loc.code})
                            </Option>
                        ))}
                    </Select>
                </Form.Item>

                <Form.Item name="destinationLocationCode" rules={[{required: true}]}>
                    <Select placeholder="Select Destination" style={{width: 180}}>
                        {locations.map((loc) => (
                            <Option key={loc.id} value={loc.code}>
                                {loc.name} ({loc.code})
                            </Option>
                        ))}
                    </Select>
                </Form.Item>

                <Form.Item name="type" rules={[{required: true}]}>
                    <Select placeholder="Type" style={{width: 120}}>
                        <Option value="FLIGHT">Flight</Option>
                        <Option value="UBER">Uber</Option>
                        <Option value="SUBWAY">Subway</Option>
                        <Option value="BUS">Bus</Option>
                    </Select>
                </Form.Item>

                <Form.Item name="operatingDays" rules={[{required: true}]}>
                    <Checkbox.Group options={daysOfWeek}/>
                </Form.Item>

                <Form.Item>
                    <Space>
                        <Button type="primary" htmlType="submit">
                            {editingId ? "Update" : "Add"}
                        </Button>
                        {editingId && (
                            <Button onClick={handleCancelEdit} danger>
                                Cancel
                            </Button>
                        )}
                    </Space>
                </Form.Item>
            </Form>

            <Table
                dataSource={transportations}
                columns={columns}
                rowKey="id"
                pagination={false}
            />
        </div>
    );
};

export default TransportationPage;