import React, { useEffect, useState } from "react";
import { useLoaderData, useNavigate, useParams } from "react-router";
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
import { PutFuncionario } from "../../services/FuncionariosService";
import { dicionarioDeRotas } from "../../ultil/DicionarioDeRotas";
import { AppIcons } from "../../ultil/DicionariosDeIcones";

export const EditarFuncionarios: React.FC = () => {
    const navigate = useNavigate();
    const { id } = useParams<{ id: string }>();
    const loaderData = (useLoaderData() as any) || {};

    const funcionarioData =
        loaderData.records || loaderData.funcionario || loaderData;
    const cargosList: ICargos[] =
        loaderData.cargos || loaderData.records?.cargos || [];
    const departamentosList: IDepartamentos[] =
        loaderData.departamentos || loaderData.records?.departamentos || [];

    const [nome, setNome] = useState("");
    const [cpf, setCpf] = useState("");
    const [vinculos, setVinculos] = useState<IVinculoItem[]>([]);
    const [carregando, setCarregando] = useState(false);

    // Modal
    const [modalAberto, setModalAberto] = useState(false);
    const [indexEdicao, setIndexEdicao] = useState<number | null>(null);

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

    useEffect(() => {
        if (funcionarioData) {
            setNome(funcionarioData.nome || "");
            setCpf(funcionarioData.cpf || "");
            setVinculos(funcionarioData.vinculos || []);
        }
    }, [funcionarioData]);

    const handleNovoVinculo = () => {
        setIndexEdicao(null);
        setEmpresaModal("");
        setMatriculaModal("");
        setCargoModal("");
        setDepartamentoModal("");
        setModalAberto(true);
    };

    const handleEditarVinculo = (vinculo: IVinculoItem, index: number) => {
        setIndexEdicao(index);
        setEmpresaModal(vinculo.empresa || "");
        setMatriculaModal(vinculo.matricula || "");
        setCargoModal(vinculo.cargo || "");
        setDepartamentoModal(vinculo.departamento || "");
        setModalAberto(true);
    };

    const handleExcluirVinculo = (index: number) => {
        if (vinculos.length <= 1) {
            alert("Não é permitido excluir o único vínculo do funcionário.");
            return;
        }
        setVinculos(vinculos.filter((_, idx) => idx !== index));
    };

    const handleSalvarVinculoModal = () => {
        if (
            !empresaModal.trim() ||
            !matriculaModal.trim() ||
            !cargoModal ||
            !departamentoModal
        ) {
            alert("Preencha todos os campos do vínculo.");
            return;
        }

        const item: IVinculoItem = {
            empresa: empresaModal,
            matricula: matriculaModal,
            cargo: cargoModal,
            departamento: departamentoModal,
            cpf,
        };

        if (indexEdicao !== null) {
            const atualizados = [...vinculos];
            atualizados[indexEdicao] = item;
            setVinculos(atualizados);
        } else {
            setVinculos([...vinculos, item]);
        }

        setModalAberto(false);
    };

    const handleSubmitFinal = async (e: React.FormEvent) => {
        e.preventDefault();
        if (vinculos.length === 0) {
            alert("O funcionário deve possuir pelo menos um vínculo.");
            return;
        }

        try {
            setCarregando(true);
            await PutFuncionario(id || cpf, { nome, cpf, vinculos });
            navigate(dicionarioDeRotas.funcionarios.listar);
        } catch (error) {
            console.error("Erro ao atualizar:", error);
            alert("Erro ao salvar as alterações do funcionário.");
        } finally {
            setCarregando(false);
        }
    };

    const colunas: ColunaTabela<IVinculoItem>[] = [
        {
            key: "editar",
            titulo: "Editar",
            width: "80px",
            align: "center",
            render: (v: IVinculoItem, index: number) => (
                <button
                    type="button"
                    className="dixi-tabela__btn-icon"
                    onClick={() => handleEditarVinculo(v, index)}
                >
                    <AppIcons.Editar />
                </button>
            ),
        },
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
            key: "excluir",
            titulo: "Excluir",
            width: "80px",
            align: "center",
            render: (_: IVinculoItem, index: number) => (
                <button
                    type="button"
                    className="dixi-tabela__btn-icon"
                    onClick={() => handleExcluirVinculo(index)}
                >
                    <AppIcons.Fechar />
                </button>
            ),
        },
    ];

    return (
        <ListagemLayout
            titulo="Editar Funcionário"
            subtitulo="Altere as informações deste funcionário"
        >
            <form onSubmit={handleSubmitFinal}>
                <Card title="Informações Gerais">
                    <div className="dixi-form-cadastro__row">
                        <Input
                            label="Nome do Funcionário"
                            value={nome}
                            onChange={e => setNome(e.target.value)}
                        />
                        <Input label="CPF" value={cpf} disabled />
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
                            onClick={handleNovoVinculo}
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
                    {indexEdicao !== null ? "Editar Vínculo" : "Novo Vínculo"}
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
                        onClick={handleSalvarVinculoModal}
                    >
                        <AppIcons.Confirmar /> Confirmar
                    </Button>
                </div>
            </Modal>
        </ListagemLayout>
    );
};

export default EditarFuncionarios;
