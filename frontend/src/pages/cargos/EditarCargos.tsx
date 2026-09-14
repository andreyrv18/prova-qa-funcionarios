import React, {useEffect, useState} from "react";
import {useLoaderData, useNavigate, useParams} from "react-router";
import ListagemLayout from "../../components/layout/ListagenLayout/ListagemLayout";
import Card from "../../components/card/Card";
import Input from "../../components/input/Input";
import Button from "../../components/button/Button";
import {PutCargo} from "../../services/CargosService";
import {dicionarioDeRotas} from "../../ultil/DicionarioDeRotas";
import type {ICargos} from "../../interfaces/Interfaces";
import {AppIcons} from "../../ultil/DicionariosDeIcones.ts";

export const EditarCargos: React.FC = () => {
    const navigate = useNavigate();
    const {id} = useParams<{ id: string }>();
    const cargoData = useLoaderData() as ICargos;

    const [descricao, setDescricao] = useState("");
    const [codigo, setCodigo] = useState("");
    const [erroDescricao, setErroDescricao] = useState("");
    const [erroCodigo, setErroCodigo] = useState("");
    const [carregando, setCarregando] = useState(false);

    useEffect(() => {
        if (cargoData) {
            setDescricao(cargoData.descricaoDoCargo || "");
            setCodigo(cargoData.codigoDoCargo || "");
        }
    }, [cargoData]);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        let valido = true;
        if (!descricao.trim()) {
            setErroDescricao("A descrição do cargo é obrigatória.");
            valido = false;
        }
        if (!codigo.trim()) {
            setErroCodigo("O código do cargo é obrigatório.");
            valido = false;
        }

        if (!valido || !id) return;

        try {
            setCarregando(true);
            await PutCargo(id, {descricao, codigo});
            navigate(dicionarioDeRotas.cargos.listar);
        } catch (error) {
            console.error("Erro ao atualizar cargo:", error);
            setErroDescricao("Falha ao salvar as alterações. Tente novamente.");
        } finally {
            setCarregando(false);
        }
    };

    return (
        <ListagemLayout
            titulo="Editar Cargo"
            subtitulo="Altere as informações deste cargo"
        >
            <Card title="Informações Gerais">
                <form onSubmit={handleSubmit} className="dixi-form-cadastro">
                    <div className="dixi-form-cadastro__row">
                        <Input
                            label="Descrição do Cargo"
                            placeholder="Insira o nome do cargo"
                            value={descricao}
                            onChange={(e) => {
                                setDescricao(e.target.value);
                                if (erroDescricao) setErroDescricao("");
                            }}
                            error={erroDescricao}
                        />
                        <Input
                            label="Código do Cargo"
                            placeholder="0000000000"
                            value={codigo}
                            onChange={(e) => {
                                setCodigo(e.target.value);
                                if (erroCodigo) setErroCodigo("");
                            }}
                            error={erroCodigo}
                        />
                    </div>

                    <div className="dixi-form-cadastro__actions">
                        <Button
                            type="button"
                            variant="outline"
                            size="medium"
                            onClick={() => navigate(dicionarioDeRotas.cargos.listar)}
                            disabled={carregando}
                        >
                            <AppIcons.Fechar/>
                            Cancelar
                        </Button>

                        <Button
                            type="submit"
                            variant="filled"
                            size="medium"
                            disabled={carregando}
                        >
                            <AppIcons.Salvar/>
                            {carregando ? "Salvando..." : "Confirmar"}
                        </Button>
                    </div>
                </form>
            </Card>
        </ListagemLayout>
    );
};

export default EditarCargos;
