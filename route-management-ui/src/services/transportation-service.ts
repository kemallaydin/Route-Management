import {API_BASE_URL} from "../components/constants/constant.ts";

export interface Transportation {
    id?: string;
    originLocationCode: string;
    destinationLocationCode: string;
    type: string;
    operatingDays: number[];
}

export interface PageInfo {
    pageNumber: number;
    pageSize: number;
    totalElements: number;
    totalPages: number;
    last: boolean;
}

export interface TransportationResponse extends PageInfo {
    content: Transportation[];
}

const BASE_URL = `${API_BASE_URL}/transportations`;

export const getTransportations = async (token: string): Promise<TransportationResponse> => {
    const response = await fetch(BASE_URL, {
        headers: {
            Authorization: `Bearer ${token}`,
        },
    });

    if (!response.ok) {
        throw new Error("Failed to get transportations");
    }

    return (await response.json()) as TransportationResponse;
};

export const createTransportation = async (
    data: Transportation,
    token: string
): Promise<Transportation> => {
    const response = await fetch(BASE_URL, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(data),
    });

    if (!response.ok) {
        throw new Error("Failed to create transportation");
    }

    return await response.json();
};

export const updateTransportation = async (
    id: string,
    data: Transportation,
    token: string
): Promise<void> => {
    const response = await fetch(`${BASE_URL}/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(data),
    });

    if (!response.ok) {
        throw new Error("Failed to update transportation");
    }
};

export const deleteTransportation = async (id: string, token: string): Promise<void> => {
    const response = await fetch(`${BASE_URL}/${id}`, {
        method: "DELETE",
        headers: {
            Authorization: `Bearer ${token}`,
        },
    });

    if (!response.ok) {
        throw new Error("Failed to delete transportation");
    }
};