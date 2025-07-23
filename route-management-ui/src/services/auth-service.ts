import {API_BASE_URL} from "../components/constants/constant.ts";

export interface LoginResponse {
    token: string;
}

const BASE_URL = `${API_BASE_URL}/auth/login`;

export async function login(username: string, password: string): Promise<LoginResponse> {
    const res = await fetch(BASE_URL, {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({username, password}),
    });

    if (!res.ok) {
        throw new Error("Login failed");
    }

    const data = await res.json();
    return data as LoginResponse;
}