alt_roleta_simples = function(pop, txCruz){
  
  # Gera populacao de pais pela taxa de cruzamento
  tamanho  = round(nrow(pop)*(txCruz/100))
  if (tamanho %% 2 != 0) { tamanho = tamanho+1 }
  popPais = matrix(nrow = tamanho,ncol = ncol(pop))
  
  # gera vetor de areas fac*Rank
  ord_pop = order(pop[,6])
  fac = sample(1:100,1) # gera peso aleatorio
  vet_areas = c(rep(NA,nrow(pop)))
  
  for(i in ord_pop){
    vet_areas[i] = fac*i
  }
  
  # ordena vetor de area (roleta) por individuo da pop
  vetAreas = vet_areas[ord_pop]
  
  nInd = nrow(popPais)
  somaTot = sum(vetAreas)
  somaInd = cumsum(vetAreas)
  
  # Gera um valor aleatorio que varia de 0 ate valor da soma da area
  # E verifica nas soma cumulativas qual soma de cromossomos e posicao resulta em valor >= randVal
  # e adiciona o individo na pop de Pais
  for(i in 1:tamanho){
    randVal = sample(0:somaTot,1);
    for(j in 1:nInd){
      if(somaInd[j] > randVal || j >= nInd){
        popPais[i, ] = pop[j,]
        break()
      }
    }
  }
  return(popPais)
}
