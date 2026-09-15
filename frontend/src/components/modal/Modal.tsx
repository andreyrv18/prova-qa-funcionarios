import React from "react";

interface ModalProps {
    isOpen: boolean;
    onClose: () => void;
    children: React.ReactNode;
    closeOnOverlayClick?: boolean;
}

export const Modal: React.FC<ModalProps> = ({
    isOpen,
    onClose,
    children,
    closeOnOverlayClick = false,
}) => {
    if (!isOpen) return null;
    const handleOverlayClick = () => {
        // Só chama o onClose se a flag estiver ativada
        if (closeOnOverlayClick) {
            onClose();
        }
    };
    return (
        <div
            className="dixi-modal-overlay"
            onClick={handleOverlayClick}
            style={{
                position: "fixed",
                top: 0,
                left: 0,
                width: "100vw",
                height: "100vh",
                backgroundColor: "rgba(0, 0, 0, 0.4)",
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
                zIndex: 1000,
            }}
        >
            <div
                style={{
                    backgroundColor: "#ffffff",
                    borderRadius: "12px",
                    padding: "32px",
                    maxWidth: "650px",
                    width: "90%",
                    boxShadow: "0 8px 32px rgba(0, 0, 0, 0.15)",
                }}
                onClick={e => e.stopPropagation()}
            >
                {children}
            </div>
        </div>
    );
};

export default Modal;
