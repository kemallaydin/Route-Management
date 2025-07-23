import {API_BASE_URL} from "../components/constants/constant.ts";

export interface Location {
    id?: string;
    code: string;
    name: string;
    country: string;
    city: string;
}

export interface PageInfo {
    pageNumber: number;
    pageSize: number;
    totalElements: number;
    totalPages: number;
    last: boolean;
}

export interface LocationResponse extends PageInfo {
    content: Location[];
}

const BASE_URL = `${API_BASE_URL}/locations`;

export const getLocations = async (token: string): Promise<LocationResponse> => {
    const response = await fetch(BASE_URL, {
        headers: {
            Authorization: `Bearer ${token}`,
        },
    });

    if (!response.ok) {
        throw new Error("Failed to get locations");
    }

    return (await response.json()) as LocationResponse;
};

export const addLocation = async (data: Location, token: string): Promise<Location> => {
    const response = await fetch(BASE_URL, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(data),
    });

    if (!response.ok) {
        throw new Error("Failed to create location");
    }

    return await response.json();
};

export const updateLocation = async (id: string, data: Location, token: string): Promise<void> => {
    const response = await fetch(`${BASE_URL}/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(data),
    });

    if (!response.ok) {
        throw new Error("Failed to update location");
    }
};

export const deleteLocation = async (id: string, token: string): Promise<void> => {
    const response = await fetch(`${BASE_URL}/${id}`, {
        method: "DELETE",
        headers: {
            Authorization: `Bearer ${token}`,
        },
    });

    if (!response.ok) {
        throw new Error("Failed to delete location");
    }
};