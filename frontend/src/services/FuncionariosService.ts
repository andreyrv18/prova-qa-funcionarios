import type {IFuncionarios,IPageResponse} from "../interfaces/Interfaces.ts";
import {API_BASE_URL,dicionarioDeRotas} from "../ultil/DicionarioDeRotas.ts";

export const GetFuncionariosList = async (): Promise<IFuncionarios[]> => {
    const response = await fetch(dicionarioDeRotas.api.funcionarios, {
        method: "GET",
    });

    if (!response.ok) {
        throw await response.json();
    }

    const dataJson = await response.json();
    console.info(dataJson);
    return dataJson;
};

export const GetFuncionariosPaginado = async (): Promise<
    IPageResponse<IFuncionarios>
> => {
    const response = await fetch(dicionarioDeRotas.api.funcionarios, {
        method: "GET",
    });

    if (!response.ok) {
        throw await response.json();
    }

    const dataJson = await response.json();
    console.info(dataJson);
    return dataJson;
};

export const getFuncionarioById = async (
    id: string,
): Promise<IFuncionarios> => {
    const response = await fetch(`${dicionarioDeRotas.api.funcionarios}/${id}`, {
        method: "GET",
    });
    if (!response.ok) {
        throw await response.json();
    }
    const dataJson = await response.json();
    console.info(dataJson);
    return dataJson;
};

export const PostFuncionarios = async (payload: object) => {
    const response = await fetch(`${API_BASE_URL}/funcionarios`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
    });

    return await response.json();
};

export const PutFuncionario = async (cpf: string, funcionarioDTO: object) => {
    const response = await fetch(`${dicionarioDeRotas.api.funcionarios}/${cpf}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(funcionarioDTO),
    });

    if (!response.ok) {
        throw await response.json();
    }
    return await response.json();
};
