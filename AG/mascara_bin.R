# Equipe: Camila Novaes Silva (201606840055)
#         Gustavo Moraes Vasconcelos (201606840014)
#         Pablo Wesley de Aguiar e Silva (201606840051)

mascara_bin = function (popPais){
  popFilhos = matrix(nrow = nrow(popPais),ncol = ncol(popPais))
  for (i in seq(1, nrow(popFilhos), by=2)) {
    # cria a mascara binaria
    mascara = sample(0:1,5,replace = T)
  
    # verifica se e 0 ou 1 para a troca de alelos
    for(j in 1:length(mascara)){
      if( mascara[j] == 1){
        popFilhos[i,j]=popPais[i,j]
        popFilhos[i+1,j]=popPais[i+1,j]
      }
      else{
        popFilhos[i,j]=popPais[i+1,j]
        popFilhos[i+1,j]=popPais[i,j]
      }
    }
    
    popFilhos[i,6] = calcularFitness(popFilhos[i,1:5])
    popFilhos[i+1,6] = calcularFitness(popFilhos[i+1,1:5])
  }
  return(popFilhos)
}