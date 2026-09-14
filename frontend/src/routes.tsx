import {createBrowserRouter, Navigate} from "react-router";
import App from "./App.tsx";
import PaginaErro from "./pages/PaginaErro.tsx";
import PaginaFuncionarios from "./pages/funcionarios/PaginaFuncionarios.tsx";
import PaginaDepartamentos from "./pages/departamentos/PaginaDepartamentos.tsx";
import PaginaCargos from "./pages/cargos/PaginaCargos.tsx";
import {getFuncionarioById, GetFuncionariosList} from "./services/FuncionariosService.ts";
import {dicionarioDeRotas} from "./ultil/DicionarioDeRotas.ts";
import EditarFuncionarios from "./pages/funcionarios/EditarFuncionarios.tsx";
import {GetCargos, GetCargosById} from "./services/CargosService.ts";
import type {ICargos, IDepartamentos, IFuncionarios} from "./interfaces/Interfaces.ts";
import {GetDepartamentoById, GetDepartamentos} from "./services/DepartamentosService.ts";
import EditarCargos from "./pages/cargos/EditarCargos.tsx";
import EditarDepartamentos from "./pages/departamentos/EditarDepartamentos.tsx";
import {GetVinculoByCpf} from "./services/VinculosService.ts";
import CadastrarFuncionarios from "./pages/funcionarios/CadastrarFuncionarios.tsx";
import CadastrarCargos from "./pages/cargos/CadastrarCargos.tsx";
import CadastrarDepartamentos from "./pages/departamentos/CadastrarDepartamentos.tsx";

export const router = createBrowserRouter([
    {
        path: "/",
        element: <App/>,
        HydrateFallback: () => <div>Carregando aplicação...</div>,
        errorElement: <PaginaErro/>,
        children: [
            {
                index: true,
                element: <Navigate to={dicionarioDeRotas.cargos.listar} replace/>,
            },
            {
                id: "rota-funcionarios",
                path: dicionarioDeRotas.funcionarios.listar,
                element: <PaginaFuncionarios/>,
                loader: async (): Promise<{ records: IFuncionarios[] }> => {
                    return {records: await GetFuncionariosList()};
                },
            },
            {
                id: "rota-funcionarios-cadastrar",
                path: dicionarioDeRotas.funcionarios.cadastrar,
                element: <CadastrarFuncionarios/>,
            },
            {
                id: "rota-funcionarios-editar",
                path: dicionarioDeRotas.funcionarios.id,
                element: <EditarFuncionarios/>,
                loader: async ({params}): Promise<{}> => {
                    const cpf = params.id;
                    if (!cpf) throw new Error("CPF não fornecido");
                    const [funcionario, listaVinculos] = await Promise.all([
                        getFuncionarioById(cpf),
                        GetVinculoByCpf(cpf),
                    ]);
                    return {
                        records: {
                            ...funcionario,
                            vinculos: listaVinculos,
                        },
                    };
                },
            },
            {
                id: "rota-cargos",
                path: dicionarioDeRotas.cargos.listar,
                element: <PaginaCargos/>,
                loader: async (): Promise<{ records: ICargos[] }> => {
                    return {records: await GetCargos()};
                },
            },
            {
                id: "rota-cargos-cadastrar",
                path: dicionarioDeRotas.cargos.cadastrar,
                element: <CadastrarCargos/>,
            },
            {
                path: dicionarioDeRotas.cargos.id, // ex: "/cargos/:id"
                element: <EditarCargos/>,
                loader: async ({params}) => GetCargosById(params.id!),
            },

            {
                id: "rota-departamentos",
                path: dicionarioDeRotas.departamentos.listar,
                element: <PaginaDepartamentos/>,
                loader: async (): Promise<{ records: IDepartamentos[] }> => {
                    return {records: await GetDepartamentos()};
                },
            },
            {
                id: "rota-departamentos-cadastrar",
                path: dicionarioDeRotas.departamentos.cadastrar,
                element: <CadastrarDepartamentos/>,
            },
            {
                path: dicionarioDeRotas.departamentos.id, // ex: "/departamentos/:id"
                element: <EditarDepartamentos/>,
                loader: async ({params}) => GetDepartamentoById(params.id!),
            }
        ],
    },
]);
