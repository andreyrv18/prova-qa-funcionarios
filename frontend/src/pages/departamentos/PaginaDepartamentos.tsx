import React, { useState } from "react";
import { useLoaderData, useNavigate } from "react-router";
import ListagemLayout from "../../components/layout/ListagenLayout/ListagemLayout";
import Card from "../../components/card/Card";
import Input from "../../components/input/Input";
import Button from "../../components/button/Button";
import Tabela from "../../components/tabela/Tabela";
import { type ColunaTabela } from "../../components/tabela/Tabela.types.ts";
import type { IDepartamentos } from "../../interfaces/Interfaces";
import { dicionarioDeRotas } from "../../ultil/DicionarioDeRotas";
import { AppIcons } from "../../ultil/DicionariosDeIcones.ts";
import { baixarRelatorioCsv } from "../../ultil/relatorioService.ts";

export const PaginaDepartamentos: React.FC = () => {
    const loaderData = useLoaderData() as { records: IDepartamentos[] };
    const navigate = useNavigate();

    const [filtroNome, setFiltroNome] = useState("");
    const [filtroCodigo, setFiltroCodigo] = useState("");

    const departamentos = loaderData?.records || [];

    // Filtro em tempo real por nome/descrição e código
    const departamentosFiltrados = departamentos.filter(dep => {
        const nome = (dep.descricaoDoDepartamento || "").toLowerCase();
        const codigo = String(dep.codigoDoDepartamento || "");
        return (
            nome.includes(filtroNome.toLowerCase()) &&
            codigo.includes(filtroCodigo)
        );
    });

    // Configuração das colunas para a tabela de departamentos
    const colunas: ColunaTabela<IDepartamentos>[] = [
        {
            key: "editar",
            titulo: "Editar",
            width: "80px",
            align: "center",
            render: departamento => (
                <button
                    className="dixi-tabela__btn-icon"
                    onClick={() =>
                        navigate(
                            dicionarioDeRotas.departamentos.id.replace(
                                ":id",
                                String(departamento.codigoDoDepartamento),
                            ),
                        )
                    }
                    title="Editar Departamento"
                >
                    <AppIcons.Editar />
                </button>
            ),
        },
        {
            key: "nome",
            titulo: "Nome",
            render: departamento => departamento.descricaoDoDepartamento,
        },
        {
            key: "id",
            titulo: "Código",
            render: departamento =>
                String(departamento.codigoDoDepartamento).padStart(16, "0"),
        },
    ];
    const handleExportarRelatorio = () => {
        baixarRelatorioCsv(
            `${dicionarioDeRotas.api.departamentos}/relatorio`,
            { filtro: filtroNome, fildesc: filtroCodigo },
            "relatorio_departamentos.csv",
        );
    };
    return (
        <ListagemLayout
            titulo="Departamentos"
            subtitulo="Veja os departamentos cadastrados no sistema."
            actions={
                <>
                    <Button
                        variant="outline"
                        size="medium"
                        onClick={handleExportarRelatorio}
                    >
                        <AppIcons.Download />
                        Baixar Relatório
                    </Button>
                    <Button
                        variant="outline"
                        size="medium"
                        onClick={() =>
                            navigate(dicionarioDeRotas.departamentos.cadastrar)
                        }
                    >
                        <AppIcons.Adicionar />
                        Novo Departamento
                    </Button>
                </>
            }
        >
            <Card>
                {/* Filtros da tabela */}
                <div
                    style={{
                        display: "flex",
                        gap: "16px",
                        marginBottom: "20px",
                    }}
                >
                    <Input
                        label="Descrição do Departamento"
                        placeholder="Procure pelo nome do departamento"
                        value={filtroNome}
                        onChange={e => setFiltroNome(e.target.value)}
                    />
                    <Input
                        label="Código"
                        placeholder="Procure pelo código do departamento"
                        value={filtroCodigo}
                        onChange={e => setFiltroCodigo(e.target.value)}
                    />
                </div>

                {/* Tabela de listagem */}
                <Tabela
                    colunas={colunas}
                    dados={departamentosFiltrados}
                    keyExtractor={(dep, index) =>
                        dep.codigoDoDepartamento ??
                        dep.descricaoDoDepartamento ??
                        index
                    }
                />
            </Card>
        </ListagemLayout>
    );
};

export default PaginaDepartamentos;
