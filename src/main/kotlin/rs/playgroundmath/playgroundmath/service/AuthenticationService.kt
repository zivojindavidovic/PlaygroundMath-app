package rs.playgroundmath.playgroundmath.service

import rs.playgroundmath.playgroundmath.payload.request.AuthenticationRequest
import rs.playgroundmath.playgroundmath.payload.response.AuthenticationResponse

interface AuthenticationService {

    fun authenticate(authRequest: AuthenticationRequest): AuthenticationResponse
}