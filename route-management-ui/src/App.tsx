import React, {useEffect, useMemo, useState} from "react";
import {Button, Divider, Input, Layout, Menu, message, Space, Typography,} from "antd";
import {LoginOutlined, LogoutOutlined, UserOutlined,} from "@ant-design/icons";

import Location from "./components/location/location";
import Route from "./components/route/route";
import Transportation from "./components/transportation/transportation";

import {parseJwt} from "./utils/jwt";
import {login as loginService} from "./services/auth-service";

import {useAuth} from "./context/auth-context";

const {Header, Sider, Content} = Layout;
const {Title, Text} = Typography;

const App: React.FC = () => {
    const [collapsed, setCollapsed] = useState(false);
    const [selectedKey, setSelectedKey] = useState<string>("");

    const [usernameInput, setUsernameInput] = useState<string>("");
    const [passwordInput, setPasswordInput] = useState<string>("");

    const {token, roles, username, login, logout} = useAuth();

    const menuItems = useMemo(() => {
        if (roles.includes("ADMIN")) {
            return [
                {key: "1", label: "Locations"},
                {key: "2", label: "Transportations"},
                {key: "3", label: "Routes"},
            ];
        } else if (roles.includes("AGENCY")) {
            return [{key: "3", label: "Routes"}];
        } else {
            return [];
        }
    }, [roles]);

    useEffect(() => {
        if (
            menuItems.length > 0 &&
            !menuItems.find((item) => item.key === selectedKey)
        ) {
            setSelectedKey(menuItems[0].key);
        } else if (menuItems.length === 0) {
            setSelectedKey("");
        }
    }, [menuItems, selectedKey]);

    const handleLogin = async () => {
        try {
            const res = await loginService(usernameInput, passwordInput);
            const decoded = parseJwt(res.token);
            if (decoded && decoded.roles) {
                const extractedRoles = decoded.roles.map((r: string) =>
                    r.replace("ROLE_", "")
                );
                login(res.token, decoded.sub || "", extractedRoles);
                setUsernameInput("");
                setPasswordInput("");
                message.success("Login successful");
            } else {
                message.error("Failed to extract roles from token.");
            }
        } catch (err) {
            message.error("Login failed");
        }
    };

    const handleLogout = () => {
        logout();
        setSelectedKey("");
        message.info("Logged out");
    };

    const renderContent = () => {
        switch (selectedKey) {
            case "1":
                return <Location/>;
            case "2":
                return <Transportation/>;
            case "3":
                return <Route/>;
            default:
                return <div>Please Login</div>;
        }
    };

    return (
        <Layout style={{height: "100vh"}}>
            <Sider
                collapsible
                collapsed={collapsed}
                onCollapse={setCollapsed}
                style={{display: "flex", flexDirection: "column"}}
            >
                <div
                    style={{
                        height: 64,
                        margin: "16px",
                        color: "white",
                        fontSize: collapsed ? 20 : 24,
                        fontWeight: "bold",
                        textAlign: "center",
                        lineHeight: "64px",
                        userSelect: "none",
                        overflow: "hidden",
                        whiteSpace: "nowrap",
                        textOverflow: "ellipsis",
                    }}
                >
                    {collapsed ? "RM" : "Route Management"}
                </div>

                {token && (
                    <div
                        style={{
                            padding: collapsed ? "0 12px" : "0 16px",
                            textAlign: "center",
                        }}
                    >
                        <div
                            style={{
                                display: "flex",
                                justifyContent: "center",
                                alignItems: "center",
                                gap: 8,
                            }}
                        >
                            <UserOutlined style={{color: "white"}}/>
                            {!collapsed && (
                                <Text style={{color: "white", fontWeight: "bold"}} ellipsis>
                                    {username}
                                </Text>
                            )}
                        </div>
                        <Divider
                            style={{borderColor: "rgba(255,255,255,0.2)", margin: "12px 0"}}
                        />
                    </div>
                )}

                <Menu
                    theme="dark"
                    mode="inline"
                    selectedKeys={[selectedKey]}
                    onClick={({key}) => setSelectedKey(key)}
                    items={menuItems}
                    style={{flex: 1, overflowY: "auto"}}
                />

                <div
                    style={{
                        padding: "16px",
                        borderTop: "1px solid rgba(255, 255, 255, 0.1)",
                        textAlign: "center",
                    }}
                >
                    {token ? (
                        <Button
                            icon={<LogoutOutlined/>}
                            danger
                            onClick={handleLogout}
                            style={{width: collapsed ? 40 : "100%"}}
                            title="Log out"
                        >
                            {!collapsed && "Log out"}
                        </Button>
                    ) : (
                        !collapsed && (
                            <div
                                style={{
                                    color: "rgba(255,255,255,0.6)",
                                    fontSize: 14,
                                }}
                            >
                                Please login
                            </div>
                        )
                    )}
                </div>
            </Sider>

            <Layout>
                <Header
                    style={{
                        background: "#fff",
                        padding: "0 16px",
                        display: "flex",
                        justifyContent: "space-between",
                        alignItems: "center",
                    }}
                >
                    <Title level={4} style={{margin: 0}}>
                        Route Management UI
                    </Title>

                    {!token && (
                        <Space>
                            <Input
                                placeholder="Username"
                                value={usernameInput}
                                onChange={(e) => setUsernameInput(e.target.value)}
                                style={{width: 150}}
                            />
                            <Input.Password
                                placeholder="Password"
                                value={passwordInput}
                                onChange={(e) => setPasswordInput(e.target.value)}
                                style={{width: 150}}
                            />
                            <Button
                                type="primary"
                                icon={<LoginOutlined/>}
                                onClick={handleLogin}
                            >
                                Login
                            </Button>
                        </Space>
                    )}
                </Header>

                <Content style={{margin: 16, padding: 24, background: "#fff"}}>
                    {renderContent()}
                </Content>
            </Layout>
        </Layout>
    );
};

export default App;