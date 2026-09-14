import "./Card.css";
import type { CardProps } from "./Card.types";

export const Card = ({ title, children, className = "" }: CardProps) => {
    return (
        <div className={`dixi-card ${className}`}>
            {title && <h2 className="dixi-card__title">{title}</h2>}
            <div className="dixi-card__content">{children}</div>
        </div>
    );
};

export default Card;
