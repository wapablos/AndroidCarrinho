mascara_bin = function (popPais){
  
  popFilhos = matrix(nrow = nrow(popPais),ncol = ncol(popPais))
  
  for (i in seq(1, nrow(popFilhos), by=2)) {
    #cria a mascara binaria
    mascara = sample(0:1,5,replace = T)
    print(mascara)
    # ver
    for(j in which(mascara == 1)){
      popFilhos[i,j]=popPais[i,j]
      popFilhos[i+1,j]=popPais[i+1,j]
    }
    
    for(j in which(mascara == 0)){
      popFilhos[i,j]=popPais[i+1,j]
      popFilhos[i+1,j]=popPais[i,j]
    }
    
    popFilhos[i,6] = calcularFitness(popFilhos[i,1:5])
    popFilhos[i+1,6] = calcularFitness(popFilhos[i+1,1:5])
  }
  return(popFilhos)
}