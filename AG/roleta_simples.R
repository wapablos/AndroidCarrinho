# Equipe: Camila Novaes Silva (201606840055)
#         Gustavo Moraes Vasconcelos (201606840014)
#         Pablo Wesley de Aguiar e Silva (201606840051)

roleta_simples = function(pop, txCruz){
  
  # Gera populacao de pais pela taxa de cruzamento
  tamanho  = round(nrow(pop)*(txCruz/100))
  if (tamanho %% 2 != 0) { tamanho = tamanho+1 }
  popPais = matrix(nrow = tamanho,ncol = ncol(pop))
  
  # Gera vetor de areas fac*Rank 
  indSum = sum(seq(1,nrow(pop)))
  fac= 100/indSum
  vet_areas = c(rep(NA,nrow(pop)))
  
  for(i in seq(1,nrow(pop))){
    vet_areas[i] = fac*i
  }
  
  # Ordena vetor de area (roleta) por rank desc individo
  vet_areas = vet_areas[order(vet_areas,decreasing = T)]
  
  nInd = nrow(popPais)
  somaTot = sum(vet_areas)
  somaInd = cumsum(vet_areas)
  
  # Gera um valor aleatorio que varia de 1 ate valor da soma da area
  # E verifica nas soma cumulativas qual soma de cromossomos e posicao resulta em valor >= randVal
  # e adiciona o individo na pop de Pais
  for(i in 1:tamanho){
    randVal = runif(1,1,somaTot);
    for(j in 1:nInd){
      if(somaInd[j] > randVal || j >= nInd){
        popPais[i, ] = pop[j,]
        break()
      }
    }
  }
  
  return(popPais)
}
