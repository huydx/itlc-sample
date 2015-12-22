def f(s: String) = "f(" + s + ")"
def g(s: String) = "g(" + s + ")"



//with compose
val fComposeG = f _ compose g _
println(fComposeG("yay"))

//without compose
val temp = g("yay")
val temp2 = f(temp)
println(temp2)
