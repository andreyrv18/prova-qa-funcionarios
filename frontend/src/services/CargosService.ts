import type {ICargos} from "../interfaces/Interfaces.ts";
import {dicionarioDeRotas} from "../ultil/DicionarioDeRotas.ts";

export const GetCargos = async (): Promise<ICargos[]> => {
    const response = await fetch(`${dicionarioDeRotas.api.cargos}/listar`, {
        method: "GET",
    });
    const dataJson = await response.json();
    console.info(dataJson);
    return dataJson;
};

export const GetCargosById = async (id: string): Promise<ICargos> => {
    const response = await fetch(`${dicionarioDeRotas.api.cargos}/${id}`, {
        method: "GET",
    });
    if (!response.ok) throw await response.json();
    return response.json();
};

export const CadastrarCargo = async (cargo: {
    descricao: string;
    codigo: string;
}): Promise<ICargos> => {
    // Mapeamento compatível com o Spring Boot (Entity/DTO)
    const payload = {

        descricaoDoCargo: cargo.descricao,

        codigoDoCargo: cargo.codigo,
    };

    const response = await fetch(`${dicionarioDeRotas.api.cargos}/criar`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(payload),
    });

    if (!response.ok) {
        const errorDetail = await response.text();
        console.error(`Erro Backend [${response.status}]:`, errorDetail);
        throw new Error(`Erro ao cadastrar cargo (Status ${response.status})`);
    }

    return await response.json();
};

export const PutCargo = async (id: string, cargo: { descricao: string; codigo: string }): Promise<ICargos> => {
    const payload = {
        descricaoDoCargo: cargo.descricao,
        codigoDoCargo: cargo.codigo,
    };

    const response = await fetch(`${dicionarioDeRotas.api.cargos}/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(payload),
    });

    if (!response.ok) {
        const errorDetail = await response.text();
        console.error(`Erro Backend [${response.status}]:`, errorDetail);
        throw new Error(`Erro ao atualizar o cargo (Status ${response.status})`);
    }

    return await response.json();
};
