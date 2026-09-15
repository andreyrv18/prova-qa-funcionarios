import React from "react";
import Navbar from "../../navbar/NavBar";
import "./ListagemLayout.css";

interface ListagemLayoutProps {
    titulo: string;
    subtitulo?: string;
    actions?: React.ReactNode;
    children: React.ReactNode;
}

export const ListagemLayout: React.FC<ListagemLayoutProps> = ({
    titulo,
    subtitulo,
    actions,
    children,
}) => {
    return (
        <div className="dixi-listagem-layout">
            <Navbar />
            <main className="dixi-listagem-layout__main">
                <div className="dixi-listagem-layout__container">
                    <header className="dixi-listagem-layout__header">
                        <div className="dixi-listagem-layout__title-group">
                            <h1 className="dixi-listagem-layout__title">
                                {titulo}
                            </h1>
                            {subtitulo && (
                                <p className="dixi-listagem-layout__subtitle">
                                    {subtitulo}
                                </p>
                            )}
                        </div>
                        {actions && (
                            <div className="dixi-listagem-layout__actions">
                                {actions}
                            </div>
                        )}
                    </header>
                    <section className="dixi-listagem-layout__content">
                        {children}
                    </section>
                </div>
            </main>
        </div>
    );
};

export default ListagemLayout;
