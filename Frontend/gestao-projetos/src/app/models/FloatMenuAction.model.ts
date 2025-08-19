export interface FloatMenuAction {
  label: string;
  icon: string;
  action: () => void;
  visible?: boolean;
  disabled?: boolean;
  color?: 'primary' | 'secondary' | 'success' | 'danger' | 'warning' | 'info';
}

export interface FloatMenuConfig {
  actions: FloatMenuAction[];
  position?: 'bottom-right' | 'bottom-left' | 'top-right' | 'top-left';
  size?: 'small' | 'medium' | 'large';
}
