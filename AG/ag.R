ag = function (tamanho, geracoes){
  source("iniciarPop.R")
  source("torneio.R")
  source("alt_roleta_simples.R")
  source("mascara_bin.R")
  source ("funcaoDeAvaliacao.R")
  source ("pontoCorte.R")
  source ("mutacao.R")
  
  melhor = 0; media = 0
  pop = iniciarPop(tamanho)
  for (i in 1:geracoes){
    pop = pop[order(pop[,6], decreasing = T),]
    #popPais = torneio (pop, 50, 3)
    popPais = alt_roleta_simples(pop, 50)
    #popFilhos = pontoCorte(popPais)
    popFilhos = mascara_bin(popPais)
    inicio = (nrow(pop) - nrow(popFilhos))+1
    pop[inicio:nrow(pop),] = popFilhos
    pop = mutacao(pop,10)
    melhor[i] = pop[1,6]
    media[i] = mean(pop[,6])
  }
  plot(melhor, col = "red", ylim = c(min(media), max(melhor)), type="l", ylab = "")
  par(new=T)
  plot(media, col = "blue", ylim = c(min(media), max(melhor)), type="l", ylab = "fitness")
  #print(popPais)
  return(pop)
}
