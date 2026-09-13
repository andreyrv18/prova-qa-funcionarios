import { dicionarioDeRotas } from "../ultil/DicionarioDeRotas.ts";

export const GetFuncionarios = async (): Promise<[]> => {
    const response = await fetch(dicionarioDeRotas.api.funcionarios, {
        method: "GET",
    });
    const dataJson = await response.json();
    console.info(dataJson);
    return dataJson;
};
