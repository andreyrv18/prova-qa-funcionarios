import { NavLink } from "react-router";
import "./Navbar.css";
import type { NavbarProps, NavItemKey } from "./Navbar.types";
import { AppIcons } from "../../ultil/DicionariosDeIcones";
import { dicionarioDeRotas } from "../../ultil/DicionarioDeRotas";
import type { JSX } from "react";

const navItems: {
    key: NavItemKey;
    label: string;
    path: string;
    icon: JSX.Element;
}[] = [
    {
        key: "funcionario",
        label: "Funcionário",
        path: dicionarioDeRotas.funcionarios.listar,
        icon: <AppIcons.Funcionario />,
    },
    {
        key: "cargo",
        label: "Cargo",
        path: dicionarioDeRotas.cargos.listar,
        icon: <AppIcons.Cargo />,
    },
    {
        key: "departamento",
        label: "Departamento",
        path: dicionarioDeRotas.departamentos.listar,
        icon: <AppIcons.Departamento />,
    },
];

export const Navbar = ({ activeItem }: NavbarProps) => {
    return (
        <aside className="dixi-navbar">
            <div className="dixi-navbar__logo">
                <svg width="40" height="40" viewBox="0 0 40 40" fill="none">
                    <path
                        d="M8 8L20 20L8 32M32 8L20 20L32 32"
                        stroke="white"
                        strokeWidth="5"
                        strokeLinecap="round"
                        strokeLinejoin="round"
                    />
                </svg>
            </div>

            <nav className="dixi-navbar__menu">
                {navItems.map(item => (
                    <NavLink
                        key={item.key}
                        to={item.path}
                        className={({ isActive }) =>
                            `dixi-navbar__item ${isActive || activeItem === item.key ? "dixi-navbar__item--active" : ""}`
                        }
                    >
                        <span className="dixi-navbar__icon">{item.icon}</span>
                        <span className="dixi-navbar__label">{item.label}</span>
                    </NavLink>
                ))}
            </nav>
        </aside>
    );
};

export default Navbar;
