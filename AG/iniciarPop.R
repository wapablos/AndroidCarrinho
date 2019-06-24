iniciarPop = function (tamanho){
  genes = matrix(nrow = tamanho, ncol = 6)
  for (i in 1:tamanho){
    genes[i,1:5] = sample(x = 4:192, size = 5, replace = T) 
    genes[i,6] = calcularFitness(genes[i,1:5])
  } 
  return(genes)
}
