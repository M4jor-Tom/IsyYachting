/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import RentalPriceDetailComponent from '@/entities/rental-price/rental-price-details.vue';
import RentalPriceClass from '@/entities/rental-price/rental-price-details.component';
import RentalPriceService from '@/entities/rental-price/rental-price.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('RentalPrice Management Detail Component', () => {
    let wrapper: Wrapper<RentalPriceClass>;
    let comp: RentalPriceClass;
    let rentalPriceServiceStub: SinonStubbedInstance<RentalPriceService>;

    beforeEach(() => {
      rentalPriceServiceStub = sinon.createStubInstance<RentalPriceService>(RentalPriceService);

      wrapper = shallowMount<RentalPriceClass>(RentalPriceDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { rentalPriceService: () => rentalPriceServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundRentalPrice = { id: 123 };
        rentalPriceServiceStub.find.resolves(foundRentalPrice);

        // WHEN
        comp.retrieveRentalPrice(123);
        await comp.$nextTick();

        // THEN
        expect(comp.rentalPrice).toBe(foundRentalPrice);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundRentalPrice = { id: 123 };
        rentalPriceServiceStub.find.resolves(foundRentalPrice);

        // WHEN
        comp.beforeRouteEnter({ params: { rentalPriceId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.rentalPrice).toBe(foundRentalPrice);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        comp.previousState();
        await comp.$nextTick();

        expect(comp.$router.currentRoute.fullPath).toContain('/');
      });
    });
  });
});
