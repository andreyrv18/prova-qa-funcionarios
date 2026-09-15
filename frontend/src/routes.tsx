import { createBrowserRouter } from "react-router";
import App from "./App.tsx";
import PaginaErro from "./pages/PaginaErro.tsx";
import PaginaFuncionarios from "./pages/funcionarios/PaginaFuncionarios.tsx";
import PaginaDepartamentos from "./pages/departamentos/PaginaDepartamentos.tsx";
import PaginaCargos from "./pages/cargos/PaginaCargos.tsx";
import {
    getFuncionarioById,
    GetFuncionariosList,
} from "./services/FuncionariosService.ts";
import { dicionarioDeRotas } from "./ultil/DicionarioDeRotas.ts";
import EditarFuncionarios from "./pages/funcionarios/EditarFuncionarios.tsx";
import { GetCargos, GetCargosById } from "./services/CargosService.ts";
import type {
    ICargos,
    IDepartamentos,
    IFuncionarios,
} from "./interfaces/Interfaces.ts";
import {
    GetDepartamentoById,
    GetDepartamentos,
} from "./services/DepartamentosService.ts";
import EditarCargos from "./pages/cargos/EditarCargos.tsx";
import EditarDepartamentos from "./pages/departamentos/EditarDepartamentos.tsx";
import CadastrarFuncionarios from "./pages/funcionarios/CadastrarFuncionarios.tsx";
import CadastrarCargos from "./pages/cargos/CadastrarCargos.tsx";
import CadastrarDepartamentos from "./pages/departamentos/CadastrarDepartamentos.tsx";

export const router = createBrowserRouter([
    {
        path: "/",
        element: <App />,
        HydrateFallback: () => <div>Carregando aplicação...</div>,
        errorElement: <PaginaErro />,
        children: [
            {
                index: true,
                element: <PaginaFuncionarios />,
            },
            {
                id: "rota-funcionarios",
                path: dicionarioDeRotas.funcionarios.listar,
                element: <PaginaFuncionarios />,
                loader: async (): Promise<{
                    records: IFuncionarios[];
                    cargos: ICargos[];
                    departamentos: IDepartamentos[];
                }> => {
                    const [records, cargos, departamentos] = await Promise.all([
                        GetFuncionariosList(),
                        GetCargos(),
                        GetDepartamentos(),
                    ]);
                    return { records, cargos, departamentos };
                },
            },
            {
                id: "rota-funcionarios-cadastrar",
                path: dicionarioDeRotas.funcionarios.cadastrar,
                element: <CadastrarFuncionarios />,
                loader: async (): Promise<{
                    cargos: ICargos[];
                    departamentos: IDepartamentos[];
                }> => {
                    const [cargos, departamentos] = await Promise.all([
                        GetCargos(),
                        GetDepartamentos(),
                    ]);
                    return { cargos, departamentos };
                },
            },
            {
                id: "rota-funcionarios-editar",
                path: dicionarioDeRotas.funcionarios.id,
                element: <EditarFuncionarios />,
                loader: async ({
                    params,
                }): Promise<{
                    records: IFuncionarios;
                    cargos: ICargos[];
                    departamentos: IDepartamentos[];
                }> => {
                    const cpf = params.id;
                    if (!cpf) throw new Error("CPF não fornecido");

                    const [funcionario, cargos, departamentos] =
                        await Promise.all([
                            getFuncionarioById(cpf),
                            GetCargos(),
                            GetDepartamentos(),
                        ]);

                    return {
                        records: funcionario,
                        cargos,
                        departamentos,
                    };
                },
            },
            {
                id: "rota-cargos",
                path: dicionarioDeRotas.cargos.listar,
                element: <PaginaCargos />,
                loader: async (): Promise<{ records: ICargos[] }> => {
                    return { records: await GetCargos() };
                },
            },
            {
                id: "rota-cargos-cadastrar",
                path: dicionarioDeRotas.cargos.cadastrar,
                element: <CadastrarCargos />,
            },
            {
                path: dicionarioDeRotas.cargos.id,
                element: <EditarCargos />,
                loader: async ({ params }) => GetCargosById(params.id!),
            },
            {
                id: "rota-departamentos",
                path: dicionarioDeRotas.departamentos.listar,
                element: <PaginaDepartamentos />,
                loader: async (): Promise<{ records: IDepartamentos[] }> => {
                    return { records: await GetDepartamentos() };
                },
            },
            {
                id: "rota-departamentos-cadastrar",
                path: dicionarioDeRotas.departamentos.cadastrar,
                element: <CadastrarDepartamentos />,
            },
            {
                path: dicionarioDeRotas.departamentos.id,
                element: <EditarDepartamentos />,
                loader: async ({ params }) => GetDepartamentoById(params.id!),
            },
        ],
    },
]);
