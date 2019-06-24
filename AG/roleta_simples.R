roleta_simples = function(pop, txCruz){
  
  # Gera populacao de pais pela taxa de cruzamento
  tamanho  = round(nrow(pop)*(txCruz/100))
  if (tamanho %% 2 != 0) { tamanho = tamanho+1 }
  popPais = matrix(nrow = tamanho,ncol = ncol(pop))
  
  # Realiza soma total das fitness e soma cumulativa
  nInd = nrow(popPais)
  somaFit = sum(pop[,6])
  somaInd = cumsum(pop[,6])

  # Gera um valor aleatorio que varia de 0 ate valor da soma total fitness
  # E verifica nas soma cumulativas qual soma de cromossomos e posicao resulta em valor >= randVal
  # e adiciona o individo na pop de Pais
  for(i in 1:tamanho){
    randVal = sample(:somaFit,1);
    for(j in 1:nInd){
      if(somaInd[j] > randVal || j >= nInd){
        popPais[i, ] = pop[j,]
        break()
      }
    }
  }
  return(popPais)
}
