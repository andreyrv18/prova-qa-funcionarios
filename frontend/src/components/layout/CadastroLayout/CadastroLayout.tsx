import "./CadastroLayout.css";
import type { CadastroLayoutProps } from "./CadastroLayout.types.ts";
import Navbar from "../../navbar/NavBar.tsx";
import Card from "../../card/Card.tsx";
import Button from "../../button/Button.tsx";
import { AppIcons } from "../../../ultil/DicionariosDeIcones.ts";

export const CadastroLayout = ({
    title,
    subtitle,
    cardTitle = "Informações Gerais",
    activeMenu,
    onCancel,
    onConfirm,
    cancelLabel = "Cancelar",
    confirmLabel = "Confirmar",
    children,
}: CadastroLayoutProps) => {
    return (
        <div className="dixi-cadastro-layout">
            {/* Sidebar Fixo com redirecionamento de rotas */}
            <Navbar activeItem={activeMenu} />

            <main className="dixi-cadastro-layout__main">
                <div className="dixi-cadastro-layout__container">
                    <header className="dixi-cadastro-layout__header">
                        <h1 className="dixi-cadastro-layout__title">{title}</h1>
                        <p className="dixi-cadastro-layout__subtitle">
                            {subtitle}
                        </p>
                    </header>

                    <Card title={cardTitle}>{children}</Card>

                    <footer className="dixi-cadastro-layout__actions">
                        <Button
                            variant="outline"
                            size="medium"
                            icon={<AppIcons.Fechar />}
                            onClick={onCancel}
                        >
                            {cancelLabel}
                        </Button>
                        <Button
                            variant="filled"
                            size="medium"
                            icon={<AppIcons.Confirmar />}
                            onClick={onConfirm}
                        >
                            {confirmLabel}
                        </Button>
                    </footer>
                </div>
            </main>
        </div>
    );
};

export default CadastroLayout;
