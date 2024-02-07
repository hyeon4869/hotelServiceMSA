
import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router);


import ReservationReservationManager from "./components/listers/ReservationReservationCards"
import ReservationReservationDetail from "./components/listers/ReservationReservationDetail"

import FrontFrontManager from "./components/listers/FrontFrontCards"
import FrontFrontDetail from "./components/listers/FrontFrontDetail"

import RoomRoomManager from "./components/listers/RoomRoomCards"
import RoomRoomDetail from "./components/listers/RoomRoomDetail"

import PaymentPaymentManager from "./components/listers/PaymentPaymentCards"
import PaymentPaymentDetail from "./components/listers/PaymentPaymentDetail"


import UserUserManager from "./components/listers/UserUserCards"
import UserUserDetail from "./components/listers/UserUserDetail"


export default new Router({
    // mode: 'history',
    base: process.env.BASE_URL,
    routes: [
            {
                path: '/reservations/reservations',
                name: 'ReservationReservationManager',
                component: ReservationReservationManager
            },
            {
                path: '/reservations/reservations/:id',
                name: 'ReservationReservationDetail',
                component: ReservationReservationDetail
            },

            {
                path: '/fronts/fronts',
                name: 'FrontFrontManager',
                component: FrontFrontManager
            },
            {
                path: '/fronts/fronts/:id',
                name: 'FrontFrontDetail',
                component: FrontFrontDetail
            },

            {
                path: '/rooms/rooms',
                name: 'RoomRoomManager',
                component: RoomRoomManager
            },
            {
                path: '/rooms/rooms/:id',
                name: 'RoomRoomDetail',
                component: RoomRoomDetail
            },

            {
                path: '/payments/payments',
                name: 'PaymentPaymentManager',
                component: PaymentPaymentManager
            },
            {
                path: '/payments/payments/:id',
                name: 'PaymentPaymentDetail',
                component: PaymentPaymentDetail
            },


            {
                path: '/users/users',
                name: 'UserUserManager',
                component: UserUserManager
            },
            {
                path: '/users/users/:id',
                name: 'UserUserDetail',
                component: UserUserDetail
            },



    ]
})
