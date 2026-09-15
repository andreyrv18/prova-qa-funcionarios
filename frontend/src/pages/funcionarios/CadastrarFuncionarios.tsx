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
    IVinculoItem,
} from "../../interfaces/Interfaces";
import { PostFuncionarios } from "../../services/FuncionariosService";
import { dicionarioDeRotas } from "../../ultil/DicionarioDeRotas";
import { AppIcons } from "../../ultil/DicionariosDeIcones";

const validarCPF = (cpf: string): boolean => {
    const cleanCpf = cpf.replace(/\D/g, "");
    if (cleanCpf.length !== 11 || /^(\d)\1{10}$/.test(cleanCpf)) return false;

    let soma = 0;
    for (let i = 0; i < 9; i++) soma += parseInt(cleanCpf.charAt(i)) * (10 - i);
    let resto = 11 - (soma % 11);
    let digito1 = resto >= 10 ? 0 : resto;
    if (digito1 !== parseInt(cleanCpf.charAt(9))) return false;

    soma = 0;
    for (let i = 0; i < 10; i++)
        soma += parseInt(cleanCpf.charAt(i)) * (11 - i);
    resto = 11 - (soma % 11);
    let digito2 = resto >= 10 ? 0 : resto;
    return digito2 === parseInt(cleanCpf.charAt(10));
};

export const CadastrarFuncionarios: React.FC = () => {
    const navigate = useNavigate();
    const loaderData = (useLoaderData() as any) || {};

    // Extração segura independentemente da estrutura retornada pelo loader
    const cargosList: ICargos[] =
        loaderData.cargos ||
        loaderData.records?.cargos ||
        loaderData.data?.cargos ||
        [];

    const departamentosList: IDepartamentos[] =
        loaderData.departamentos ||
        loaderData.records?.departamentos ||
        loaderData.data?.departamentos ||
        [];

    const [nome, setNome] = useState("");
    const [cpf, setCpf] = useState("");
    const [vinculos, setVinculos] = useState<IVinculoItem[]>([]);
    const [carregando, setCarregando] = useState(false);

    const [erroNome, setErroNome] = useState("");
    const [erroCpf, setErroCpf] = useState("");

    // Modal Vínculo
    const [modalAberto, setModalAberto] = useState(false);
    const [empresaModal, setEmpresaModal] = useState("");
    const [matriculaModal, setMatriculaModal] = useState("");
    const [cargoModal, setCargoModal] = useState("");
    const [departamentoModal, setDepartamentoModal] = useState("");

    const optionsCargos = cargosList.map((c: any) => {
        const valor =
            typeof c === "string"
                ? c
                : c.descricaoDoCargo ||
                  c.nome ||
                  c.descricao ||
                  c.cargo ||
                  String(c.codigoDoCargo || c.id || "");
        return { value: valor, label: valor };
    });

    const optionsDepartamentos = departamentosList.map((d: any) => {
        const valor =
            typeof d === "string"
                ? d
                : d.descricaoDoDepartamento ||
                  d.nome ||
                  d.descricao ||
                  d.departamento ||
                  String(d.codigoDoDepartamento || d.id || "");
        return { value: valor, label: valor };
    });

    const handleAdicionarVinculo = () => {
        if (
            !empresaModal.trim() ||
            !matriculaModal.trim() ||
            !cargoModal ||
            !departamentoModal
        ) {
            alert("Preencha todos os campos do vínculo.");
            return;
        }

        setVinculos([
            ...vinculos,
            {
                empresa: empresaModal,
                matricula: matriculaModal,
                cargo: cargoModal,
                departamento: departamentoModal,
            },
        ]);
        setModalAberto(false);
        setEmpresaModal("");
        setMatriculaModal("");
        setCargoModal("");
        setDepartamentoModal("");
    };

    const handleRemoverVinculo = (index: number) => {
        setVinculos(vinculos.filter((_, idx) => idx !== index));
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        let valido = true;

        if (!nome.trim()) {
            setErroNome("O nome é obrigatório.");
            valido = false;
        }

        if (!cpf.trim() || !validarCPF(cpf)) {
            setErroCpf("Informe um CPF válido.");
            valido = false;
        }

        if (vinculos.length === 0) {
            alert("O funcionário deve possuir pelo menos um vínculo.");
            return;
        }

        if (!valido) return;

        try {
            setCarregando(true);
            await PostFuncionarios({ nome, cpf, vinculos });
            navigate(dicionarioDeRotas.funcionarios.listar);
        } catch (error) {
            console.error("Erro ao cadastrar funcionário:", error);
            alert(
                "Erro ao cadastrar funcionário. Verifique se o CPF é duplicado.",
            );
        } finally {
            setCarregando(false);
        }
    };

    const colunas: ColunaTabela<IVinculoItem>[] = [
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
        {
            key: "acoes",
            titulo: "Ações",
            align: "center",
            render: (_: IVinculoItem, index: number) => (
                <button
                    type="button"
                    className="dixi-tabela__btn-icon"
                    onClick={() => handleRemoverVinculo(index)}
                >
                    <AppIcons.Fechar />
                </button>
            ),
        },
    ];

    return (
        <ListagemLayout
            titulo="Cadastrar Funcionário"
            subtitulo="Insira as informações do novo funcionário"
        >
            <form onSubmit={handleSubmit}>
                <Card title="Informações Gerais">
                    <div className="dixi-form-cadastro__row">
                        <Input
                            label="Nome do Funcionário"
                            value={nome}
                            onChange={e => setNome(e.target.value)}
                            error={erroNome}
                        />
                        <Input
                            label="CPF"
                            placeholder="000.000.000-00"
                            value={cpf}
                            onChange={e => setCpf(e.target.value)}
                            error={erroCpf}
                        />
                    </div>
                </Card>

                <div style={{ marginTop: "24px" }}></div>

                <Card
                    title="Empresas"
                    action={
                        <Button
                            type="button"
                            variant="outline"
                            size="small"
                            onClick={() => setModalAberto(true)}
                        >
                            <AppIcons.Adicionar /> Novo Vínculo
                        </Button>
                    }
                >
                    <Tabela
                        colunas={colunas}
                        dados={vinculos}
                        keyExtractor={(v: IVinculoItem, idx: number) =>
                            `${v.matricula}-${idx}`
                        }
                    />
                </Card>

                <div
                    className="dixi-form-cadastro__actions"
                    style={{ marginTop: "24px" }}
                >
                    <Button
                        type="button"
                        variant="outline"
                        size="medium"
                        onClick={() =>
                            navigate(dicionarioDeRotas.funcionarios.listar)
                        }
                    >
                        <AppIcons.Fechar /> Cancelar
                    </Button>
                    <Button
                        type="submit"
                        variant="filled"
                        size="medium"
                        disabled={carregando}
                    >
                        <AppIcons.Salvar />{" "}
                        {carregando ? "Salvando..." : "Salvar"}
                    </Button>
                </div>
            </form>

            <Modal isOpen={modalAberto} onClose={() => setModalAberto(false)}>
                <h2
                    style={{
                        fontSize: "20px",
                        color: "#0066B3",
                        marginBottom: "20px",
                    }}
                >
                    Novo Vínculo
                </h2>
                <div
                    style={{
                        display: "grid",
                        gridTemplateColumns: "1fr 1fr",
                        gap: "16px",
                        marginBottom: "16px",
                    }}
                >
                    <Input
                        label="Nome da Empresa"
                        value={empresaModal}
                        onChange={e => setEmpresaModal(e.target.value)}
                    />
                    <Input
                        label="Matrícula"
                        value={matriculaModal}
                        onChange={e => setMatriculaModal(e.target.value)}
                    />
                </div>
                <div
                    style={{
                        display: "grid",
                        gridTemplateColumns: "1fr 1fr",
                        gap: "16px",
                        marginBottom: "24px",
                    }}
                >
                    <Combobox
                        label="Cargo"
                        placeholder="Selecione Uma Opção"
                        options={optionsCargos}
                        value={cargoModal}
                        onChange={(val: string) => setCargoModal(val)}
                    />
                    <Combobox
                        label="Departamento"
                        placeholder="Selecione Uma Opção"
                        options={optionsDepartamentos}
                        value={departamentoModal}
                        onChange={(val: string) => setDepartamentoModal(val)}
                    />
                </div>
                <div
                    style={{
                        display: "flex",
                        justifyContent: "center",
                        gap: "16px",
                    }}
                >
                    <Button
                        type="button"
                        variant="outline"
                        size="medium"
                        onClick={() => setModalAberto(false)}
                    >
                        <AppIcons.Fechar /> Cancelar
                    </Button>
                    <Button
                        type="button"
                        variant="filled"
                        size="medium"
                        onClick={handleAdicionarVinculo}
                    >
                        <AppIcons.Confirmar /> Confirmar
                    </Button>
                </div>
            </Modal>
        </ListagemLayout>
    );
};

export default CadastrarFuncionarios;
