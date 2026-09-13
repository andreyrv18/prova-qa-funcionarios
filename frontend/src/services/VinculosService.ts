import { dicionarioDeRotas } from "../ultil/DicionarioDeRotas.ts";

export const GetVinculos = async (): Promise<[]> => {
    const response = await fetch(dicionarioDeRotas.api.vinculos, {
        method: "GET",
    });
    const dataJson = await response.json();
    console.info(dataJson);
    return dataJson;
};
