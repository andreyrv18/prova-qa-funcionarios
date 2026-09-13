import { dicionarioDeRotas } from "../ultil/DicionarioDeRotas.ts";

export const GetCargos = async (): Promise<[]> => {
    const response = await fetch(dicionarioDeRotas.api.cargos, {
        method: "GET",
    });
    const dataJson = await response.json();
    console.info(dataJson);
    return dataJson;
};
