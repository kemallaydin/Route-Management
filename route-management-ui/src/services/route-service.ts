import {API_BASE_URL} from "../components/constants/constant.ts";

export type Step = {
    order: number;
    type: string;
    from: string;
    to: string;
};

export type Route = {
    route: Step[];
};

export type RouteResponse = {
    routes: Route[];
};

const BASE_URL = `${API_BASE_URL}/routes`;

export const getRoutes = async (
    originCode: string,
    destinationCode: string,
    date: string,
    token: string
): Promise<RouteResponse> => {
    const url = `${BASE_URL}?originLocationCode=${originCode}&destinationLocationCode=${destinationCode}&date=${date}`;
    const response = await fetch(url, {
        headers: {
            Authorization: `Bearer ${token}`,
        },
    });

    if (!response.ok) {
        throw new Error("Failed to get routes");
    }

    return (await response.json()) as RouteResponse;
};