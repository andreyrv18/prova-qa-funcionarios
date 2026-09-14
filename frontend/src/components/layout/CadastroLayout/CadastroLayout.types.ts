import React from "react";
import type { NavItemKey } from "../../navbar/Navbar.types.ts";

export interface CadastroLayoutProps {
    title: string;
    subtitle: string;
    cardTitle?: string;
    activeMenu: NavItemKey;
    onNavigate?: (item: NavItemKey) => void;
    onCancel?: () => void;
    onConfirm?: () => void;
    cancelLabel?: string;
    confirmLabel?: string;
    children: React.ReactNode;
}
