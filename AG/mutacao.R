mutacao = function (pop, tx){
  nMut = round(nrow(pop)*(tx/100))
  if (nMut < 1) {nMut = 1}
  
  for (i in 1:nMut){
    #com elitismo
    cromo = sample(2:nrow(pop), 1)
    locus = sample(1:5,1)
    pop[cromo, locus] = sample(4:192,1)
    pop[cromo, 6] = calcularFitness(pop[cromo,1:5])
  }
  
  return(pop)
}