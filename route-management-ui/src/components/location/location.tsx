import React, {useEffect, useState} from "react";
import {Button, Form, Input, message, Popconfirm, Space, Table} from "antd";
import {
    addLocation,
    deleteLocation,
    getLocations,
    type Location as LocationType,
    updateLocation,
} from "../../services/location-service";
import {useAuth} from "../../context/auth-context.tsx";

const Location: React.FC = () => {
    const [locations, setLocations] = useState<LocationType[]>([]);
    const [form] = Form.useForm();
    const [editingKey, setEditingKey] = useState<string | null>(null);
    const {token} = useAuth();

    useEffect(() => {
        const fetchData = async () => {
            try {
                const data = await getLocations(token);
                setLocations(data.content);
            } catch (error) {
                message.error("Failed to get locations.");
            }
        };

        fetchData();
    }, []);

    const handleAdd = async (values: Omit<LocationType, "id">) => {
        try {
            const newLocation = await addLocation(values, token);
            setLocations([...locations, newLocation]);
            form.resetFields();
            message.success("Location added successfully.");
        } catch (error) {
            message.error("Failed to add location.");
        }
    };

    const handleDelete = async (id: string) => {
        try {
            await deleteLocation(id, token);
            setLocations(locations.filter((loc) => loc.id !== id));
            message.success("Location deleted successfully.");
        } catch (error) {
            message.error("Failed to delete location.");
        }
    };

    const handleSaveEdit = async (values: Omit<LocationType, "id">) => {
        try {
            if (editingKey !== null) {
                await updateLocation(editingKey, values, token);
                setLocations(
                    locations.map((loc) =>
                        loc.id === editingKey ? {...loc, ...values} : loc
                    )
                );
                message.success("Location updated successfully.");
                setEditingKey(null);
                form.resetFields();
            }
        } catch (error) {
            message.error("Failed to update location.");
        }
    };

    const handleEdit = (record: LocationType) => {
        form.setFieldsValue(record);
        setEditingKey(record.id || null);
    };

    const handleCancelEdit = () => {
        setEditingKey(null);
        form.resetFields();
    };

    const columns = [
        {
            title: "Code",
            dataIndex: "code",
            key: "code",
        },
        {
            title: "Name",
            dataIndex: "name",
            key: "name",
        },
        {
            title: "Country",
            dataIndex: "country",
            key: "country",
        },
        {
            title: "City",
            dataIndex: "city",
            key: "city",
        },
        {
            title: "Actions",
            key: "actions",
            render: (_: any, record: LocationType) => (
                <Space>
                    <Button type="link" onClick={() => handleEdit(record)}>
                        Edit
                    </Button>
                    <Popconfirm
                        title="Are you sure you want to delete?"
                        onConfirm={() => handleDelete(record.id!)}
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
            <h2>Locations</h2>

            <Form
                form={form}
                layout="inline"
                onFinish={editingKey ? handleSaveEdit : handleAdd}
                style={{marginBottom: 16}}
            >
                <Form.Item name="code" rules={[{required: true}]}>
                    <Input placeholder="Code"/>
                </Form.Item>
                <Form.Item name="name" rules={[{required: true}]}>
                    <Input placeholder="Name"/>
                </Form.Item>
                <Form.Item name="country" rules={[{required: true}]}>
                    <Input placeholder="Country"/>
                </Form.Item>
                <Form.Item name="city" rules={[{required: true}]}>
                    <Input placeholder="City"/>
                </Form.Item>
                <Form.Item>
                    <Space>
                        <Button type="primary" htmlType="submit">
                            {editingKey ? "Update" : "Add"}
                        </Button>
                        {editingKey && (
                            <Button onClick={handleCancelEdit} danger>
                                Cancel
                            </Button>
                        )}
                    </Space>
                </Form.Item>
            </Form>

            <Table
                columns={columns}
                dataSource={locations}
                rowKey="id"
                pagination={false}
                style={{marginTop: 24}}
            />
        </div>
    );
};

export default Location;