import React, {useState} from "react";
import {useNavigate} from "react-router";
import ListagemLayout from "../../components/layout/ListagenLayout/ListagemLayout";
import Card from "../../components/card/Card";
import Input from "../../components/input/Input";
import Button from "../../components/button/Button";
import {CadastrarCargo} from "../../services/CargosService";
import {dicionarioDeRotas} from "../../ultil/DicionarioDeRotas";
import "./CadastrarCargos.css";
import {AppIcons} from "../../ultil/DicionariosDeIcones.ts";

export const CadastrarCargos: React.FC = () => {
    const navigate = useNavigate();

    const [descricao, setDescricao] = useState("");
    const [codigo, setCodigo] = useState("");
    const [erroDescricao, setErroDescricao] = useState("");
    const [erroCodigo, setErroCodigo] = useState("");
    const [carregando, setCarregando] = useState(false);

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

        if (!valido) return;

        try {
            setCarregando(true);
            await CadastrarCargo({descricao, codigo});
            navigate(dicionarioDeRotas.cargos.listar);
        } catch (error) {
            console.error("Erro ao cadastrar cargo:", error);
            setErroDescricao("Falha ao salvar o cargo. Tente novamente.");
        } finally {
            setCarregando(false);
        }
    };

    return (
        <ListagemLayout
            titulo="Cadastrar Cargo"
            subtitulo="Preencha os campos abaixo para adicionar um novo cargo ao sistema."
        >
            <Card>
                <form onSubmit={handleSubmit} className="dixi-form-cadastro">
                    <div className="dixi-form-cadastro__row">
                        <Input
                            label="Descrição do Cargo"
                            placeholder="Digite a descrição do cargo"
                            value={descricao}
                            onChange={(e) => {
                                setDescricao(e.target.value);
                                if (erroDescricao) setErroDescricao("");
                            }}
                            error={erroDescricao}
                        />
                        <Input
                            label="Código"
                            placeholder="Digite o código do cargo"
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
                            {carregando ? "Salvando..." : "Salvar"}
                        </Button>
                    </div>
                </form>
            </Card>
        </ListagemLayout>
    );
};

export default CadastrarCargos;
