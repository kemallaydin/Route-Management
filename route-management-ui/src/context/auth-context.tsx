import React, {createContext, type ReactNode, useContext, useState} from "react";

interface AuthContextType {
    token: string;
    username: string;
    roles: string[];
    login: (token: string, username: string, roles: string[]) => void;
    logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: ReactNode }> = ({children}) => {
    const [token, setToken] = useState("");
    const [username, setUsername] = useState("");
    const [roles, setRoles] = useState<string[]>([]);

    const login = (token: string, username: string, roles: string[]) => {
        setToken(token);
        setUsername(username);
        setRoles(roles);
    };

    const logout = () => {
        setToken("");
        setUsername("");
        setRoles([]);
    };

    return (
        <AuthContext.Provider value={{token, username, roles, login, logout}}>
            {children}
        </AuthContext.Provider>
    );
};

export function useAuth() {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error("useAuth must be used within an AuthProvider");
    }
    return context;
}