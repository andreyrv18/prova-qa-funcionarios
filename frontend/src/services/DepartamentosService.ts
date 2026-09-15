import type { IDepartamentos } from "../interfaces/Interfaces.ts";
import { dicionarioDeRotas } from "../ultil/DicionarioDeRotas.ts";

export const GetDepartamentos = async (): Promise<IDepartamentos[]> => {
    const response = await fetch(
        `${dicionarioDeRotas.api.departamentos}/listar`,
        {
            method: "GET",
        },
    );
    const dataJson = await response.json();
    console.info(dataJson);
    return dataJson;
};

export const GetDepartamentoById = async (
    id: string,
): Promise<IDepartamentos> => {
    const response = await fetch(
        `${dicionarioDeRotas.api.departamentos}/${id}`,
        {
            method: "GET",
        },
    );
    if (!response.ok) throw await response.json();
    return response.json();
};

export const CadastrarDepartamento = async (departamento: {
    descricao: string;
    codigo: string;
}): Promise<IDepartamentos> => {
    const payload = {
        descricaoDoDepartamento: departamento.descricao,

        codigoDoDepartamento: departamento.codigo,
    };

    const response = await fetch(
        `${dicionarioDeRotas.api.departamentos}/criar`,
        {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(payload),
        },
    );

    if (!response.ok) {
        throw new Error("Erro ao cadastrar o departamento.");
    }

    return await response.json();
};
export const PutDepartamento = async (
    id: string,
    departamento: {
        descricao: string;
        codigo: string;
    },
): Promise<IDepartamentos> => {
    const payload = {
        descricaoDoDepartamento: departamento.descricao,
        codigoDoDepartamento: departamento.codigo,
    };

    const response = await fetch(
        `${dicionarioDeRotas.api.departamentos}/${id}`,
        {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(payload),
        },
    );

    if (!response.ok) {
        const errorDetail = await response.text();
        console.error(`Erro Backend [${response.status}]:`, errorDetail);
        throw new Error(
            `Erro ao atualizar o departamento (Status ${response.status})`,
        );
    }

    return await response.json();
};
