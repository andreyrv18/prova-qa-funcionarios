export const baixarRelatorioCsv = async (
    endpoint: string,
    params: Record<string, string>,
    nomeArquivo: string,
) => {
    try {
        // Remove parâmetros vazios ou nulos da URL
        const paramsLimpos = Object.fromEntries(
            Object.entries(params).filter(
                ([_, v]) => v !== undefined && v !== null && v !== "",
            ),
        );

        const searchParams = new URLSearchParams(paramsLimpos);
        const response = await fetch(`${endpoint}?${searchParams.toString()}`, {
            method: "GET",
        });

        if (!response.ok) {
            throw new Error("Erro ao gerar o relatório.");
        }

        const blob = await response.blob();
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement("a");
        a.href = url;
        a.download = nomeArquivo;
        document.body.appendChild(a);
        a.click();
        a.remove();
        window.URL.revokeObjectURL(url);
    } catch (error) {
        console.error("Erro no download do relatório:", error);
        alert("Erro ao gerar relatório. Tente novamente.");
    }
};
