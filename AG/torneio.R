torneio = function(pop, txCruz, nTorneio){
  
  tamanho  = round(nrow(pop)*(txCruz/100))
  if (tamanho %% 2 != 0) { tamanho = tamanho+1}
  
  popPais = pop[1:tamanho,]
  for (i in 1:tamanho){
    maior =  min (pop[,6])
    for (n in 1:nTorneio){
      candidato = sample(1:nrow(pop), 1)
      if (pop[candidato,6] > maior){
        maior = pop[candidato,6]
        selecao = candidato
      }
    }
    popPais[i,] = pop[selecao,]
  }
  return (popPais)
}