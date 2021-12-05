import { Authority } from '@/shared/security/authority';
/* tslint:disable */
// prettier-ignore

// prettier-ignore
const Day = () => import('@/entities/day/day.vue');
// prettier-ignore
const DayUpdate = () => import('@/entities/day/day-update.vue');
// prettier-ignore
const DayDetails = () => import('@/entities/day/day-details.vue');
// prettier-ignore
const Client = () => import('@/entities/client/client.vue');
// prettier-ignore
const ClientUpdate = () => import('@/entities/client/client-update.vue');
// prettier-ignore
const ClientDetails = () => import('@/entities/client/client-details.vue');
// prettier-ignore
const Reservation = () => import('@/entities/reservation/reservation.vue');
// prettier-ignore
const ReservationUpdate = () => import('@/entities/reservation/reservation-update.vue');
// prettier-ignore
const ReservationDetails = () => import('@/entities/reservation/reservation-details.vue');
// prettier-ignore
const Rental = () => import('@/entities/rental/rental.vue');
// prettier-ignore
const RentalUpdate = () => import('@/entities/rental/rental-update.vue');
// prettier-ignore
const RentalDetails = () => import('@/entities/rental/rental-details.vue');
// prettier-ignore
const RentalPrice = () => import('@/entities/rental-price/rental-price.vue');
// prettier-ignore
const RentalPriceUpdate = () => import('@/entities/rental-price/rental-price-update.vue');
// prettier-ignore
const RentalPriceDetails = () => import('@/entities/rental-price/rental-price-details.vue');
// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default [
  {
    path: '/day',
    name: 'Day',
    component: Day,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/day/new',
    name: 'DayCreate',
    component: DayUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/day/:dayId/edit',
    name: 'DayEdit',
    component: DayUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/day/:dayId/view',
    name: 'DayView',
    component: DayDetails,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/client',
    name: 'Client',
    component: Client,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/client/new',
    name: 'ClientCreate',
    component: ClientUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/client/:clientId/edit',
    name: 'ClientEdit',
    component: ClientUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/client/:clientId/view',
    name: 'ClientView',
    component: ClientDetails,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/reservation',
    name: 'Reservation',
    component: Reservation,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/reservation/new',
    name: 'ReservationCreate',
    component: ReservationUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/reservation/:reservationId/edit',
    name: 'ReservationEdit',
    component: ReservationUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/reservation/:reservationId/view',
    name: 'ReservationView',
    component: ReservationDetails,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/rental',
    name: 'Rental',
    component: Rental,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/rental/new',
    name: 'RentalCreate',
    component: RentalUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/rental/:rentalId/edit',
    name: 'RentalEdit',
    component: RentalUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/rental/:rentalId/view',
    name: 'RentalView',
    component: RentalDetails,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/rental-price',
    name: 'RentalPrice',
    component: RentalPrice,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/rental-price/new',
    name: 'RentalPriceCreate',
    component: RentalPriceUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/rental-price/:rentalPriceId/edit',
    name: 'RentalPriceEdit',
    component: RentalPriceUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/rental-price/:rentalPriceId/view',
    name: 'RentalPriceView',
    component: RentalPriceDetails,
    meta: { authorities: [Authority.USER] },
  },
  // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
];
