import React, { useState } from "react";
import { useLoaderData, useNavigate } from "react-router";
import ListagemLayout from "../../components/layout/ListagenLayout/ListagemLayout";
import Card from "../../components/card/Card";
import Input from "../../components/input/Input";
import Button from "../../components/button/Button";
import Tabela from "../../components/tabela/Tabela";
import { type ColunaTabela } from "../../components/tabela/Tabela.types";
import Modal from "../../components/modal/Modal";
import Combobox from "../../components/combobox/Combobox";
import type {
    ICargos,
    IDepartamentos,
    IFuncionarios,
    IVinculoItem,
} from "../../interfaces/Interfaces";
import { dicionarioDeRotas } from "../../ultil/DicionarioDeRotas";
import { AppIcons } from "../../ultil/DicionariosDeIcones";
import { baixarRelatorioCsv } from "../../ultil/relatorioService.ts";

interface FuncionariosComVinculos extends IFuncionarios {
    vinculos?: IVinculoItem[];
}

interface LoaderData {
    records: FuncionariosComVinculos[];
    cargos?: ICargos[];
    departamentos?: IDepartamentos[];
}

export const PaginaFuncionarios: React.FC = () => {
    const {
        records: funcionarios = [],
        cargos = [],
        departamentos = [],
    } = (useLoaderData() as LoaderData) || {};

    const navigate = useNavigate();

    const [filtroNome, setFiltroNome] = useState("");
    const [filtroCpf, setFiltroCpf] = useState("");
    const [filtroMatricula, setFiltroMatricula] = useState("");
    const [filtroEmpresa, setFiltroEmpresa] = useState("");
    const [filtroCargo, setFiltroCargo] = useState("");
    const [filtroDepartamento, setFiltroDepartamento] = useState("");

    const [funcionarioSelecionado, setFuncionarioSelecionado] =
        useState<FuncionariosComVinculos | null>(null);

    // Mapeamento dinâmico de opções para os Comboboxes
    const optionsCargos = (cargos || []).map((c: any) => {
        const rotulo =
            typeof c === "string"
                ? c
                : c.descricaoDoCargo ||
                  c.nome ||
                  c.descricao ||
                  c.cargo ||
                  String(c.codigoDoCargo || "");
        return { value: rotulo, label: rotulo };
    });

    const optionsDepartamentos = (departamentos || []).map((d: any) => {
        const rotulo =
            typeof d === "string"
                ? d
                : d.descricaoDoDepartamento ||
                  d.nome ||
                  d.descricao ||
                  d.departamento ||
                  String(d.codigoDoDepartamento || "");
        return { value: rotulo, label: rotulo };
    });

    const funcionariosFiltrados = funcionarios.filter(func => {
        const atendeNome = (func.nome || "")
            .toLowerCase()
            .includes(filtroNome.toLowerCase());
        const atendeCpf = (func.cpf || "").includes(filtroCpf);

        const atendeVinculos = (func.vinculos || []).some(v => {
            const atendeMatricula =
                !filtroMatricula ||
                (v.matricula || "").includes(filtroMatricula);
            const atendeEmpresa =
                !filtroEmpresa ||
                (v.empresa || "")
                    .toLowerCase()
                    .includes(filtroEmpresa.toLowerCase());
            const atendeCargo = !filtroCargo || v.cargo === filtroCargo;
            const atendeDepto =
                !filtroDepartamento || v.departamento === filtroDepartamento;
            return (
                atendeMatricula && atendeEmpresa && atendeCargo && atendeDepto
            );
        });

        return (
            atendeNome &&
            atendeCpf &&
            (!func.vinculos?.length || atendeVinculos)
        );
    });

    const colunas: ColunaTabela<FuncionariosComVinculos>[] = [
        {
            key: "editar",
            titulo: "Editar",
            width: "80px",
            align: "center",
            render: (funcionario: FuncionariosComVinculos) => (
                <button
                    type="button"
                    className="dixi-tabela__btn-icon"
                    onClick={e => {
                        e.stopPropagation();
                        navigate(
                            dicionarioDeRotas.funcionarios.id.replace(
                                ":id",
                                String(funcionario.cpf),
                            ),
                        );
                    }}
                    title="Editar Funcionário"
                >
                    <AppIcons.Editar />
                </button>
            ),
        },
        {
            key: "nome",
            titulo: "Nome",
            render: (f: FuncionariosComVinculos) => f.nome,
        },
        {
            key: "cpf",
            titulo: "CPF",
            render: (f: FuncionariosComVinculos) => f.cpf,
        },
    ];

    const colunasVinculosModal: ColunaTabela<IVinculoItem>[] = [
        {
            key: "empresa",
            titulo: "Empresa",
            render: (v: IVinculoItem) => v.empresa,
        },
        {
            key: "matricula",
            titulo: "Matrícula",
            render: (v: IVinculoItem) => v.matricula,
        },
        { key: "cargo", titulo: "Cargo", render: (v: IVinculoItem) => v.cargo },
        {
            key: "departamento",
            titulo: "Departamento",
            render: (v: IVinculoItem) => v.departamento,
        },
    ];
    const handleExportarRelatorio = () => {
        baixarRelatorioCsv(
            `${dicionarioDeRotas.api.funcionarios}/relatorio`,
            {
                nome: filtroNome,
                cpf: filtroCpf,
                matricula: filtroMatricula,
                empresa: filtroEmpresa,
                cargo: filtroCargo,
                departamento: filtroDepartamento,
            },
            "relatorio_funcionarios.csv",
        );
    };
    return (
        <ListagemLayout
            titulo="Funcionários"
            subtitulo="Veja os funcionários cadastrados no sistema."
            actions={
                <>
                    <Button
                        variant="outline"
                        size="medium"
                        onClick={handleExportarRelatorio}
                    >
                        <AppIcons.Download /> Baixar Relatório
                    </Button>

                    <Button
                        variant="outline"
                        size="medium"
                        onClick={() =>
                            navigate(dicionarioDeRotas.funcionarios.cadastrar)
                        }
                    >
                        <AppIcons.Adicionar /> Novo Funcionário
                    </Button>
                </>
            }
        >
            <Card>
                <div
                    style={{
                        display: "grid",
                        gridTemplateColumns: "repeat(6, 1fr)",
                        gap: "12px",
                        marginBottom: "16px",
                    }}
                >
                    <Input
                        label="Nome do Funcionário"
                        placeholder="Procure pelo funcionário"
                        value={filtroNome}
                        onChange={e => setFiltroNome(e.target.value)}
                    />
                    <Input
                        label="CPF"
                        placeholder="000.000.000-00"
                        value={filtroCpf}
                        onChange={e => setFiltroCpf(e.target.value)}
                    />
                    <Input
                        label="Matrícula"
                        placeholder="0000000000"
                        value={filtroMatricula}
                        onChange={e => setFiltroMatricula(e.target.value)}
                    />
                    <Input
                        label="Empresa"
                        placeholder="Procure pela empresa"
                        value={filtroEmpresa}
                        onChange={e => setFiltroEmpresa(e.target.value)}
                    />

                    <Combobox
                        label="Cargo"
                        placeholder="Selecione Uma Opção"
                        options={optionsCargos}
                        value={filtroCargo}
                        onChange={(val: string) => setFiltroCargo(val)}
                    />

                    <Combobox
                        label="Departamento"
                        placeholder="Selecione Uma Opção"
                        options={optionsDepartamentos}
                        value={filtroDepartamento}
                        onChange={(val: string) => setFiltroDepartamento(val)}
                    />
                </div>

                <p
                    style={{
                        fontSize: "14px",
                        color: "#666",
                        marginBottom: "12px",
                        fontWeight: 500,
                    }}
                >
                    Clique para ver os vínculos de empresa do funcionário
                </p>

                <Tabela
                    colunas={colunas}
                    dados={funcionariosFiltrados}
                    keyExtractor={(func: FuncionariosComVinculos) => func.cpf}
                    onRowClick={(func: FuncionariosComVinculos) =>
                        setFuncionarioSelecionado(func)
                    }
                />
            </Card>

            <Modal
                isOpen={!!funcionarioSelecionado}
                onClose={() => setFuncionarioSelecionado(null)}
            >
                <h2
                    style={{
                        fontSize: "20px",
                        color: "#0066B3",
                        marginBottom: "20px",
                    }}
                >
                    Vínculos de Empresa
                </h2>
                <Tabela
                    colunas={colunasVinculosModal}
                    dados={funcionarioSelecionado?.vinculos || []}
                    keyExtractor={(v: IVinculoItem, idx: number) =>
                        v.matricula || String(idx)
                    }
                />
                <div
                    style={{
                        display: "flex",
                        justifyContent: "center",
                        marginTop: "24px",
                    }}
                >
                    <Button
                        variant="outline"
                        size="medium"
                        onClick={() => setFuncionarioSelecionado(null)}
                    >
                        Fechar
                    </Button>
                </div>
            </Modal>
        </ListagemLayout>
    );
};

export default PaginaFuncionarios;
