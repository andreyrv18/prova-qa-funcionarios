export interface IFuncionarios {
    cpf: string;
    nome: string;
}

export interface ICargos {
    codigoDoCargo: string;
    descricaoDoCargo: string;
}

export interface IDepartamentos {
    codigoDoDepartamento: string;
    descricaoDoDepartamento: string;
}

export interface IPageResponse<T> {
    content: T[];
    totalPages: number;
    totalElements: number;
    size: number;
    number: number;
}

export interface IVinculos {
    cargo: string;
    departamento: string;
    empresa: string;
    funicionario: string;
    matricula: number;
}

export interface IVinculoItem {
    id?: number | string;
    empresa: string;
    matricula: string;
    cargo: string;
    departamento: string;
    cpf?: string;

    [key: string]: any;
}
