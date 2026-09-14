import React from "react";
import "./Tabela.css";

export interface ColunaTabela<T> {
    key: string;
    titulo: string;
    width?: string;
    align?: "left" | "center" | "right";
    render?: (item: T) => React.ReactNode;
}

export interface TabelaProps<T> {
    colunas: ColunaTabela<T>[];
    dados: T[];
    keyExtractor: (item: T, index: number) => string | number;
    mensagemVazia?: string;
}
export const Tabela = <T,>({
                               colunas,
                               dados,
                               keyExtractor,
                               mensagemVazia = "Nenhum registro encontrado.",
                           }: TabelaProps<T>) => {
    return (
        <div className="dixi-tabela-container">
            <table className="dixi-tabela">
                <thead>
                <tr>
                    {colunas.map((coluna) => (
                        <th
                            key={coluna.key}
                            style={{
                                width: coluna.width,
                                textAlign: coluna.align || "left",
                            }}
                        >
                            {coluna.titulo}
                        </th>
                    ))}
                </tr>
                </thead>
                <tbody>
                {dados.length > 0 ? (
                    dados.map((item, index) => {
                        // Garante que a chave nunca seja undefined ou null
                        const chave = keyExtractor(item, index) ?? index;
                        return (
                            <tr key={chave}>
                                {colunas.map((coluna) => (
                                    <td
                                        key={coluna.key}
                                        style={{ textAlign: coluna.align || "left" }}
                                    >
                                        {coluna.render
                                            ? coluna.render(item)
                                            : (item as Record<string, any>)[coluna.key]}
                                    </td>
                                ))}
                            </tr>
                        );
                    })
                ) : (
                    <tr>
                        <td colSpan={colunas.length} className="dixi-tabela__vazio">
                            {mensagemVazia}
                        </td>
                    </tr>
                )}
                </tbody>
            </table>
        </div>
    );
};

export default Tabela;
