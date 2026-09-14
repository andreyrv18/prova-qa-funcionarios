import React, {useEffect, useState} from "react";
import {useLoaderData, useNavigate, useParams} from "react-router";
import ListagemLayout from "../../components/layout/ListagenLayout/ListagemLayout";
import Card from "../../components/card/Card";
import Input from "../../components/input/Input";
import Button from "../../components/button/Button";
import {PutDepartamento} from "../../services/DepartamentosService";
import {dicionarioDeRotas} from "../../ultil/DicionarioDeRotas";
import type {IDepartamentos} from "../../interfaces/Interfaces";
import {AppIcons} from "../../ultil/DicionariosDeIcones.ts";

export const EditarDepartamentos: React.FC = () => {
    const navigate = useNavigate();
    const {id} = useParams<{ id: string }>();
    const departamentoData = useLoaderData() as IDepartamentos;

    const [descricao, setDescricao] = useState("");
    const [codigo, setCodigo] = useState("");
    const [erroDescricao, setErroDescricao] = useState("");
    const [erroCodigo, setErroCodigo] = useState("");
    const [carregando, setCarregando] = useState(false);

    useEffect(() => {
        if (departamentoData) {
            setDescricao(
                departamentoData.descricaoDoDepartamento ||

                ""
            );
            setCodigo(
                departamentoData.codigoDoDepartamento ||

                ""
            );
        }
    }, [departamentoData]);
    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        let valido = true;
        if (!descricao.trim()) {
            setErroDescricao("A descrição do departamento é obrigatória.");
            valido = false;
        }
        if (!codigo.trim()) {
            setErroCodigo("O código do departamento é obrigatório.");
            valido = false;
        }

        if (!valido || !id) return;

        try {
            setCarregando(true);
            await PutDepartamento(id, {descricao, codigo});
            navigate(dicionarioDeRotas.departamentos.listar);
        } catch (error) {
            console.error("Erro ao atualizar departamento:", error);
            setErroDescricao("Falha ao salvar as alterações. Tente novamente.");
        } finally {
            setCarregando(false);
        }
    };

    return (
        <ListagemLayout
            titulo="Editar Departamento"
            subtitulo="Altere as informações deste departamento"
        >
            <Card title="Informações Gerais">
                <form onSubmit={handleSubmit} className="dixi-form-cadastro">
                    <div className="dixi-form-cadastro__row">
                        <Input
                            label="Descrição do Departamento"
                            placeholder="Insira o nome do departamento"
                            value={descricao}
                            onChange={(e) => {
                                setDescricao(e.target.value);
                                if (erroDescricao) setErroDescricao("");
                            }}
                            error={erroDescricao}
                        />
                        <Input
                            label="Código do Departamento"
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
                            onClick={() => navigate(dicionarioDeRotas.departamentos.listar)}
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

export default EditarDepartamentos;
