import React, {useEffect, useState} from "react";
import {Button, DatePicker, Form, message, Select, Table, Typography} from "antd";
import {getRoutes, type Step} from "../../services/route-service";
import {getLocations, type Location} from "../../services/location-service";
import {useAuth} from "../../context/auth-context.tsx";

const {Title} = Typography;
const {Option} = Select;

const Routes: React.FC = () => {
    const [routes, setRoutes] = useState<Step[][]>([]);
    const [locations, setLocations] = useState<Location[]>([]);
    const [form] = Form.useForm();
    const {token} = useAuth();

    useEffect(() => {
        const fetchLocations = async () => {
            try {
                const data = await getLocations(token);
                setLocations(data.content);
            } catch (error) {
                message.error("Failed to load locations.");
            }
        };

        fetchLocations();
    }, []);

    const formatDate = (date: Date) => {
        const day = String(date.getDate()).padStart(2, "0");
        const month = String(date.getMonth() + 1).padStart(2, "0");
        const year = date.getFullYear();
        return `${day}-${month}-${year}`;
    };

    const onFinish = async (values: { origin: string; destination: string; date: any }) => {
        try {
            const formattedDate = formatDate(values.date.toDate());
            const fetchedRoutes = await getRoutes(values.origin, values.destination, formattedDate, token);
            setRoutes(fetchedRoutes.routes.map((r) => r.route));
        } catch (err: any) {
            message.error(err.message);
        }
    };

    return (
        <div style={{maxHeight: "80vh", overflowY: "auto", paddingRight: 16, paddingBottom: 16}}>
            <Title level={3}>Search Routes</Title>

            <Form form={form} layout="inline" onFinish={onFinish}>
                <Form.Item
                    name="origin"
                    rules={[{required: true, message: "Origin is required"}]}
                >
                    <Select placeholder="Select Origin Location" style={{width: 180}}>
                        {locations.map((loc) => (
                            <Option key={loc.id} value={loc.code}>
                                {loc.name} ({loc.code})
                            </Option>
                        ))}
                    </Select>
                </Form.Item>

                <Form.Item
                    name="destination"
                    rules={[{required: true, message: "Destination is required"}]}
                >
                    <Select placeholder="Select Destination Location" style={{width: 180}}>
                        {locations.map((loc) => (
                            <Option key={loc.id} value={loc.code}>
                                {loc.name} ({loc.code})
                            </Option>
                        ))}
                    </Select>
                </Form.Item>

                <Form.Item
                    name="date"
                    rules={[{required: true, message: "Date is required"}]}
                >
                    <DatePicker/>
                </Form.Item>

                <Form.Item>
                    <Button type="primary" htmlType="submit">
                        Search
                    </Button>
                </Form.Item>
            </Form>

            {routes.length > 0 ? (
                routes.map((route, idx) => (
                    <div key={idx} style={{marginTop: 32}}>
                        <Title level={5}>Route #{idx + 1}</Title>
                        <Table
                            dataSource={route}
                            rowKey="order"
                            pagination={false}
                            scroll={{y: 300}}
                            columns={[
                                {title: "Step", dataIndex: "order", key: "order"},
                                {title: "Type", dataIndex: "type", key: "type"},
                                {title: "From", dataIndex: "from", key: "from"},
                                {title: "To", dataIndex: "to", key: "to"},
                            ]}
                        />
                    </div>
                ))
            ) : (
                <p style={{marginTop: 24}}>No route data yet.</p>
            )}
        </div>
    );
};

export default Routes;