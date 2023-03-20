package at.knusperleicht.kvalidator

import kotlin.test.Test
import kotlin.test.assertEquals


class Test {

    enum class Gender { FEMALE, MALE, NONE }
    data class Address(val street: String, val zip: Int, val code: Int, val gender: Gender)
    data class Customer(val firstName: String, val lastName: String, val addresses: List<Address>)




    data class DomainTest(val name: String, val domain: String) : SelfValidating<DomainTest>() {
        override fun validator(): Validator<DomainTest> = validator {
            DomainTest::name {
                notBlank() message "BLABLABL"
                length(3)
            }
            DomainTest::domain {
                notBlank()
            }
        }
    }


    @Test
    fun test() {

        val address = Address("Griechenberg", 3021, 99, Gender.FEMALE)

        val spec = validator() {

            Address::street {
                length(3, 255)
            }

            Address::code {
                min(3)
            }
            Address::zip {
                min(3) message "Das ist ein test"
                max(255) message "Maximum erreicht"
            }

            Address::gender {
                enum<Gender>()
            }


        }.validate(address)


        println(spec)

    }


    @Test
    fun testDomain() {
        val doma = DomainTest("", "")

        val errors = doma.validateSelf()

        println(errors)

        assertEquals(1, errors.size)
    }

}


