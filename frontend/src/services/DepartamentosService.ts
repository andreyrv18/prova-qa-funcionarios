import { dicionarioDeRotas } from "../ultil/DicionarioDeRotas.ts";

export const GetDepartamentos = async (): Promise<[]> => {
    const response = await fetch(dicionarioDeRotas.api.departamentos, {
        method: "GET",
    });
    const dataJson = await response.json();
    console.info(dataJson);
    return dataJson;
};
