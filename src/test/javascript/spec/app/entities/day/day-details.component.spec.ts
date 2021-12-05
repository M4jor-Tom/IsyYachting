/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import DayDetailComponent from '@/entities/day/day-details.vue';
import DayClass from '@/entities/day/day-details.component';
import DayService from '@/entities/day/day.service';
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
  describe('Day Management Detail Component', () => {
    let wrapper: Wrapper<DayClass>;
    let comp: DayClass;
    let dayServiceStub: SinonStubbedInstance<DayService>;

    beforeEach(() => {
      dayServiceStub = sinon.createStubInstance<DayService>(DayService);

      wrapper = shallowMount<DayClass>(DayDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { dayService: () => dayServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundDay = { id: 123 };
        dayServiceStub.find.resolves(foundDay);

        // WHEN
        comp.retrieveDay(123);
        await comp.$nextTick();

        // THEN
        expect(comp.day).toBe(foundDay);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundDay = { id: 123 };
        dayServiceStub.find.resolves(foundDay);

        // WHEN
        comp.beforeRouteEnter({ params: { dayId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.day).toBe(foundDay);
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
