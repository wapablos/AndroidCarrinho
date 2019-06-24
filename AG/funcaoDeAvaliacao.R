calcularFitness = function (vet){
  pMax = 500
  penalizacao = 20
  
  peso = c(2,4,5,8,12)
  valor = c(3,6,10,18,26)
  
  pesoCromossomo = t(vet) %*% peso
  valorCromossomo = t(vet) %*% valor
  
  if (pesoCromossomo < pMax){
    fitness = valorCromossomo
  } else {
    fitness =  valorCromossomo - (pesoCromossomo-pMax) * penalizacao
  }
  return (fitness)
}
