export interface PremioModel{
  id: number | null;
  // Exp.: "Prêmio de Inovação"
  nome: string;
  // Exp.: "Inovação em Tecnologia da Informação"
  descricao: string;
  // Exp.: 2022; 2026
  anoEdicao: number;
  cronogramaId: number;
}
