pontoCorte = function (popPais){
  popFilhos = popPais;
  for (i in seq(1, nrow(popFilhos), by=2)) {
    ponto = sample(1:4,1)
    #filho 1
    popFilhos[i,] = popPais[i,]
    popFilhos[i,1:ponto] = popPais[i+1,1:ponto]
    popFilhos[i,6] = calcularFitness(popFilhos[i,1:5])
    
    #filho 2
    popFilhos[i+1,] = popPais[i+1,]
    popFilhos[i+1,1:ponto] = popPais[i,1:ponto]
    popFilhos[i+1,6] = calcularFitness(popFilhos[i+1,1:5])
  }
  return(popFilhos)
}