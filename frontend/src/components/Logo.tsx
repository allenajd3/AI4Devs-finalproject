import logoAyg from '../assets/logo-ayg.png';

interface LogoProps {
  size?: 'sm' | 'md' | 'lg';
  variant?: 'default' | 'white';
  className?: string;
}

export const Logo = ({ size = 'md', variant = 'default', className = '' }: LogoProps) => {
  const sizeClasses = {
    sm: 'h-8',    // 32px
    md: 'h-12',   // 48px
    lg: 'h-16',   // 64px
  };

  // Filtro CSS para convertir el logo a blanco
  const variantClasses = variant === 'white' 
    ? 'brightness-0 invert' 
    : '';

  return (
    <img
      src={logoAyg}
      alt="a&g"
      className={`${sizeClasses[size]} w-auto ${variantClasses} ${className}`}
    />
  );
};
