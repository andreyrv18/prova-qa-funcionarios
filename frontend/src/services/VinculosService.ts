import type {IPageResponse,IVinculos} from "../interfaces/Interfaces.ts";
import {dicionarioDeRotas} from "../ultil/DicionarioDeRotas.ts";

export const GetVinculoByCpf = async (id: string): Promise<IVinculos> => {
    const response = await fetch(`${dicionarioDeRotas.api.vinculos}/${id}`, {
        method: "GET",
    });
    if (!response.ok) {
        throw await response.json();
    }
    const dataJson = await response.json();
    console.info(dataJson);
    return dataJson;
};

export const GetVinculoList = async (): Promise<IVinculos[]> => {
    const response = await fetch(dicionarioDeRotas.api.vinculos, {
        method: "GET",
    });

    if (!response.ok) {
        throw await response.json();
    }

    const dataJson = await response.json();
    console.info(dataJson);
    return dataJson;
};

export const GetVinculoPage = async (): Promise<IPageResponse<IVinculos>> => {
    const response = await fetch(dicionarioDeRotas.api.vinculosPage, {
        method: "GET",
    });

    if (!response.ok) {
        throw await response.json();
    }

    const dataJson = await response.json();
    console.info(dataJson);
    return dataJson;
};

export const PostVinculo = async (payload: object) => {
    const response = await fetch(dicionarioDeRotas.api.vinculos, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
    });

    if (!response.ok) throw await response.json();
    return await response.json();
};

export const PutVinculo = async (id: string | number, payload: object) => {
    const response = await fetch(`${dicionarioDeRotas.api.vinculos}/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
    });

    if (!response.ok) throw await response.json();
    return await response.json();
};
