import React, { useState } from "react";
import { useLoaderData, useNavigate } from "react-router";
import ListagemLayout from "../../components/layout/ListagenLayout/ListagemLayout";
import Card from "../../components/card/Card";
import Input from "../../components/input/Input";
import Button from "../../components/button/Button";
import Tabela from "../../components/tabela/Tabela";
import { type ColunaTabela } from "../../components/tabela/Tabela.types.ts";
import type { ICargos } from "../../interfaces/Interfaces";
import { dicionarioDeRotas } from "../../ultil/DicionarioDeRotas";
import { AppIcons } from "../../ultil/DicionariosDeIcones.ts";
import { baixarRelatorioCsv } from "../../ultil/relatorioService.ts";

export const PaginaCargos: React.FC = () => {
    const loaderData = useLoaderData() as { records: ICargos[] };
    const navigate = useNavigate();

    const [filtroNome, setFiltroNome] = useState("");
    const [filtroCodigo, setFiltroCodigo] = useState("");

    const cargos = loaderData?.records || [];

    // Filtro em tempo real
    const cargosFiltrados = cargos.filter(
        c =>
            c.descricaoDoCargo
                .toLowerCase()
                .includes(filtroNome.toLowerCase()) &&
            c.codigoDoCargo.toString().includes(filtroCodigo),
    );

    // Definição das colunas da tabela
    const colunas: ColunaTabela<ICargos>[] = [
        {
            key: "editar",
            titulo: "Editar",
            width: "80px",
            align: "center",
            render: cargo => (
                <button
                    className="dixi-tabela__btn-icon"
                    onClick={() =>
                        navigate(
                            dicionarioDeRotas.cargos.id.replace(
                                ":id",
                                String(cargo.codigoDoCargo),
                            ),
                        )
                    }
                    title="Editar Cargo"
                >
                    <AppIcons.Editar />
                </button>
            ),
        },
        {
            key: "nome",
            titulo: "Nome",
            render: cargo => cargo.descricaoDoCargo,
        },
        {
            key: "id",
            titulo: "Código",
            render: cargo => String(cargo.codigoDoCargo).padStart(16, "0"),
        },
    ];

    const handleExportarRelatorio = () => {
        baixarRelatorioCsv(
            `${dicionarioDeRotas.api.cargos}/relatorio`,
            { filtro: filtroNome, fildesc: filtroCodigo },
            "relatorio_cargos.csv",
        );
    };
    return (
        <ListagemLayout
            titulo="Cargos"
            subtitulo="Veja os funcionários cadastrados no sistema."
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
                            navigate(dicionarioDeRotas.cargos.cadastrar)
                        }
                    >
                        <AppIcons.Adicionar />
                        Novo Cargo
                    </Button>
                </>
            }
        >
            <Card>
                {/* Área de Filtros */}
                <div
                    style={{
                        display: "flex",
                        gap: "16px",
                        marginBottom: "20px",
                    }}
                >
                    <Input
                        label="Descrição do Cargo"
                        placeholder="Procure pelo nome do cargo"
                        value={filtroNome}
                        onChange={e => setFiltroNome(e.target.value)}
                    />
                    <Input
                        label="Código"
                        placeholder="Procure pelo código do cargo"
                        value={filtroCodigo}
                        onChange={e => setFiltroCodigo(e.target.value)}
                    />
                </div>

                {/* Tabela de Dados */}
                <Tabela
                    colunas={colunas}
                    dados={cargosFiltrados}
                    keyExtractor={(cargo, index) =>
                        cargo.codigoDoCargo ?? cargo.descricaoDoCargo ?? index
                    }
                />
            </Card>
        </ListagemLayout>
    );
};

export default PaginaCargos;
