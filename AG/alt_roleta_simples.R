pop
ord_pop = pop[order(pop[,6]),]
fac = runif(1)
vet_areas = c(rep(NA,nrow(pop)))
for(i in 1:nrow(ord_pop)){
  vet_areas[i] = fac*i
}

